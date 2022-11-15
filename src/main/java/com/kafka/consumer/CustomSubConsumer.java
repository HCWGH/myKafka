package com.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author HCW
 * @date 2022/11/14 16:04
 * @todo 订阅topic消费
 */
@Component
public class CustomSubConsumer {
    public static void main(String[] args) {

    }

    @PostConstruct
    public void consumer_1() {
        new Thread(() -> {
            Properties properties = new Properties();
            //1.设置连接broker
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "xxxxxxx:9092,xxxxxxx:9093,xxxxxxx:9094");
            //2.反序列化
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            //3.设置消费组
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "first_group");
            //4.设置消费的topic
            List<String> topicList = new ArrayList<>();
            topicList.add("mytest");
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
            kafkaConsumer.subscribe(topicList);
            while (true) {
                //1s消费一次
                ConsumerRecords<String, String> dataList = kafkaConsumer.poll(Duration.ofSeconds(1));
                dataList.forEach(data -> {
                    System.out.println("1=========partition=" + data.partition() + " topic=" + data.topic() + " value=" + data.value());
                });
            }
        }).start();
    }

    @PostConstruct
    public void consumer_2() {
        new Thread(() -> {
            Properties properties = new Properties();
            //1.设置连接broker
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "xxxxxxx:9092,xxxxxxx:9093,xxxxxxx:9094");
            //2.反序列化
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            //3.设置消费组
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "first_group");
            //4.设置消费的topic
            List<String> topicList = new ArrayList<>();
            topicList.add("mytest");
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
            kafkaConsumer.subscribe(topicList);
            while (true) {
                //1s消费一次
                ConsumerRecords<String, String> dataList = kafkaConsumer.poll(Duration.ofSeconds(1));
                dataList.forEach(data -> {
                    System.out.println("2=========partition=" + data.partition() + " topic=" + data.topic() + " value=" + data.value());
                });
            }
        }).start();
    }

}
