package com.kafka.producer;

import com.kafka.partition.MyPartition;
import org.apache.kafka.clients.producer.*;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CustomProducer {
    public static void main(String[] args) throws InterruptedException {

    }

    @Test
    /**
     * 不带回调的生产者
     */
    public void producerNoCallback() {
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "xxxxxxx:9092");
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new
                KafkaProducer<String, String>(properties);

        // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 100; i++) {
            kafkaProducer.send(new ProducerRecord<>("mytest", i + "", "myKafka_topic_data_" + i));
        }
        // 5. 关闭资源
        kafkaProducer.close();
    }

    /**
     * 带回调生产者,当producer收到ack进行回调，两个参数meta和Exception
     */
    @Test
    public void producerCallback() {
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "xxxxxxx:9092");
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new
                KafkaProducer<String, String>(properties);
        // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 100; i++) {
            //指定key
            kafkaProducer.send(new ProducerRecord<>("mytest", i + "", "myKafka_topic_data_" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.out.println("send data success callback " + "topic:" + recordMetadata.topic() + " partition:" + recordMetadata.partition());
                    } else {
                        System.out.println("send data fail");
                    }
                }
            });
        }
        // 5. 关闭资源
        kafkaProducer.close();
    }

    /**
     * 生产者分区，未指定partition和key
     * 粘性分区，随机选择一个分区
     * 当数据的大小达到batch.size（默认16kb）或者时间linger.ms(默认0)已经到时，数据就会打包发送至broker,
     * linger.ms(默认0)linger.ms=0会立马发送数据，但并不是每次只发一条数据，也是以batch方式处理，broker处理producer的请求，在这段时间内产生的所有数据一起打包
     */
    @Test
    public void producerPartition() throws InterruptedException {
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "xxxxxxx:9092");
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new
                KafkaProducer<String, String>(properties);
        // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 100; i++) {
            //未指定partition和key
            kafkaProducer.send(new ProducerRecord<>("mytest", "myKafka_topic_data_" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.out.println("send data success callback " + "topic:" + recordMetadata.topic() + " partition:" + recordMetadata.partition());
                    } else {
                        System.out.println("send data fail");
                    }
                }
            });
            //linger.ms到期重新选择分区,让producer每次发送一条数据
            TimeUnit.MILLISECONDS.sleep(800);
        }
        // 5. 关闭资源
        kafkaProducer.close();
    }

    /**
     * 指定分区，保证数据只写入一个partition
     */
    @Test
    public void producerAssignPartition() {
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "xxxxxxx:9092");
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new
                KafkaProducer<String, String>(properties);
        // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 100; i++) {
            //指定partition=1
            kafkaProducer.send(new ProducerRecord<>("mytest", 1, i + "", "myKafka_topic_data_" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.out.println("send data success callback " + "topic:" + recordMetadata.topic() + " partition:" + recordMetadata.partition());
                    } else {
                        System.out.println("send data fail");
                    }
                }
            });
        }
        // 5. 关闭资源
        kafkaProducer.close();
    }

    /**
     * 自定义producer的分区器
     */
    @Test
    public void producerCustomPartition() {
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "xxxxxxx:9092,xxxxxxx:9093,xxxxxxx:9094");
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        //指定自定义分区器
        properties.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartition.class.getName());
        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 100; i++) {
            //设置key,自定义分区器根据key进行分区
            kafkaProducer.send(new ProducerRecord<>("mytest", i + "", "myKafka_topic_data_" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.out.println("send data success callback " + "topic:" + recordMetadata.topic() + " partition:" + recordMetadata.partition());
                    } else {
                        System.out.println("send data fail");
                    }
                }
            });
        }
        // 5. 关闭资源
        kafkaProducer.close();
    }
}
