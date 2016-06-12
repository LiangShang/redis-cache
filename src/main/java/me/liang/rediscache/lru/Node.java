package me.liang.rediscache.lru;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
* This is the lru node that saves the pre, next nodes and the key of the cache
*/
public class Node {

    String key;
    String pre;
    String next;

    public Node() {
    }

    public Node( Node n) {
        this.key = n.getKey();
        this.pre = n.getPre();
        this.next = n.getNext();
    }

    public Node(String key) {
        this.key = key;
    }

    public Node(String key, String pre, String next) {
        this.key = key;
        this.next = next;
        this.pre = pre;
    }

    public String getKey() {
        return key;
    }

    public Node setKey(String key) {
        this.key = key;
        return this;
    }

    public String getNext() {
        return next;
    }

    public Node setNext(String next) {
        this.next = next;
        return this;
    }

    public String getPre() {
        return pre;
    }

    public Node setPre(String pre) {
        this.pre = pre;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Objects.equal(key, node.key) &&
                Objects.equal(pre, node.pre) &&
                Objects.equal(next, node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, pre, next);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("pre", pre)
                .add("next", next)
                .toString();
    }
}
