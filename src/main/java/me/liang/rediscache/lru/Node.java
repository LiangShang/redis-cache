package me.liang.rediscache.lru;

/**
* This is the lru node that saves the pre, next nodes and the key of the cache
*/
public class Node {

    String key;
    Node pre;
    Node next;

}
