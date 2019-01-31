package com.tpadsz;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WsBltApplicationTests {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private LogTest logTest;

    private static Logger logger = LoggerFactory.getLogger(WsBltApplicationTests.class);

    @Test
    public void contextLoads() {
        Map map= JSON.parseObject("{\"ctype\":\"71\",\"x\":\"37\",\"prefix_value\":\"03\",\"y\":\"37\"}",Map.class);
        sqlSessionTemplate.selectOne("light.saveConsole", map);
        logger.info("result=" + map.get("result"));
//        System.out.println(sqlSessionTemplate.selectList("light.getLights").size());
    }

    @Test
    public void testLog() {
        logTest.testKey();
        logTest.testLog();
    }
}

