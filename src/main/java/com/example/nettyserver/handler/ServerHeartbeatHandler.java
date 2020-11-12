package com.example.nettyserver.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.nettyserver.utilities.Utilities.CARRIAGE_RETURN_BYTE;

public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerHeartbeatHandler.class);

    private static String heartbeat_message = "Heartbeat";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // If IdleStateEvent is received, then send heartbeat message (and close on failure)
        if (evt instanceof IdleStateEvent) {
            logger.info("IdleState event is received. Heartbeat message is being fired");
            ctx.writeAndFlush(Unpooled.copiedBuffer(ArrayUtils.add(heartbeat_message.getBytes(), CARRIAGE_RETURN_BYTE)))
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

            // Else, pass the message to the next handler
        } else {
            logger.info("General event is received");
            super.userEventTriggered(ctx, evt);
        }
    }
}
