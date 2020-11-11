package com.example.nettyserver.initializer;

import com.example.nettyserver.handler.ServerChannelHandler;
import com.example.nettyserver.handler.ServerHeartbeatHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    public static final byte CARRIAGE_RETURN_BYTE = 0X0D;
    private static final Logger logger = LoggerFactory.getLogger(ServerChannelInitializer.class);
    private int delay;
    private int maxFrameLength = 700;
    private int readerHeartbeatTimeout = 0;
    private int writerHeartbeatTimeout = 0;
    private int allHeartbeatTimeout = 60;

    public ServerChannelInitializer(int delay, int readerHeartbeatTimeout, int writerHeartbeatTimeout, int allHeartbeatTimeout) {
        this.delay = delay;
        this.readerHeartbeatTimeout = readerHeartbeatTimeout;
        this.writerHeartbeatTimeout = writerHeartbeatTimeout;
        this.allHeartbeatTimeout = allHeartbeatTimeout;
    }

    public ServerChannelInitializer(int delay, int maxFrameLength, int allHeartbeatTimeout) {
        this.delay = delay;
        this.maxFrameLength = maxFrameLength;
        this.allHeartbeatTimeout = allHeartbeatTimeout;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        logger.info("Creating channel: " + channel.toString());

        byte[] carriage_return_byte_array = {CARRIAGE_RETURN_BYTE};
        ByteBuf carriage_return = Unpooled.copiedBuffer(carriage_return_byte_array);

        // Setting this server up as Delimiter based.
        DelimiterBasedFrameDecoder decoder = new DelimiterBasedFrameDecoder(maxFrameLength, false, carriage_return);
        channel.pipeline().addFirst(decoder);

        ChannelHandler channelHandler = new ServerChannelHandler(delay);
        channel.pipeline().addLast(channelHandler);

        // Setting up Heartbeat functionality for the server
        IdleStateHandler idleStateHandler = new IdleStateHandler(readerHeartbeatTimeout, writerHeartbeatTimeout, allHeartbeatTimeout, TimeUnit.SECONDS);
        channel.pipeline().addLast(idleStateHandler);
        channel.pipeline().addLast(new ServerHeartbeatHandler());
    }
}
