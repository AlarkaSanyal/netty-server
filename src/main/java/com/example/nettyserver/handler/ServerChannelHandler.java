package com.example.nettyserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.example.nettyserver.utilities.Utilities.getByteArray;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);

    private int delay;

    public ServerChannelHandler(int delay) {

        this.delay = delay;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf input = (ByteBuf) msg;
        byte[] inputArray = getByteArray(input);
        Channel channel = ctx.channel();

        // Add a delay before responding
        Thread.sleep(delay);

        logger.info("Incoming message: " + Arrays.toString(inputArray));
        byte[] outputArray = getOutput(inputArray);
        logger.info("Outgoing message: " + Arrays.toString(outputArray));

        channel.writeAndFlush(Unpooled.copiedBuffer(outputArray));
    }

    // Process response
    private byte[] getOutput(byte[] inputArray) {

        return ArrayUtils.addAll(inputArray, "Response".getBytes());
    }
}
