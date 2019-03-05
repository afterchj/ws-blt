package com.tpadsz;

import com.tpadsz.netty.ServerMain;
import com.tpadsz.socket.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WsBltApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsBltApplication.class, args);
        new NettyServer().start(8000);
        new ServerMain().run(8001);
    }

}

