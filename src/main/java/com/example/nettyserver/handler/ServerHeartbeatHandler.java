package com.example.nettyserver.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerHeartbeatHandler.class);

    private static String heartbeat_message = "This is a Heartbeat message";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // If IdleStateEvent is received, then send heartbeat message (and close on failure)
        if (evt instanceof IdleStateEvent) {
            logger.info("IdleState event is received. Heartbeat message is being fired");
            ctx.writeAndFlush(Unpooled.copiedBuffer(heartbeat_message.getBytes()))
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

            // Else, pass the message to the next handler
        } else {
            logger.info("General event is received");
            super.userEventTriggered(ctx, evt);
        }
    }
}
