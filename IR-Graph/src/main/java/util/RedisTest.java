package util;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by patricio on 21-12-15.
 */
public class RedisTest {


    public void stringTest() {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server sucessfully");
        //set the data in redis string
        jedis.set("tutorial-name", "Redis tutorial");
        // Get the stored data and print it
        System.out.println("Stored string in redis:: "+ jedis.get("tutorial-name"));

    }

    public void listTest() {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server sucessfully");
        //store data in redis list
        jedis.lpush("tutorial-list", "Redis");
        jedis.lpush("tutorial-list", "Mongodb");
        jedis.lpush("tutorial-list", "Mysql");
        // Get the stored data and print it
        List<String> list = jedis.lrange("tutorial-list", 0 ,5);
        for(int i=0; i<list.size(); i++) {
            System.out.println("Stored string in redis:: "+list.get(i));
        }

    }


    public static void main(String[] args) {

        RedisTest t = new RedisTest();
        t.stringTest();

        t.listTest();

    }


}
