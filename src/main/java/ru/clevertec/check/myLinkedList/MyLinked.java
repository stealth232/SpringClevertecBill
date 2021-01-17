package ru.clevertec.check.myLinkedList;

public interface MyLinked <E> {
    void add(E element) ;
    void addByIndex(int index, E element);
    int size();
    E remove(int index);
    E get(int index);
    void addFirst(E e);
    void addLast(E e);
    void removeFirst();
    E getFirstElem();
    E getLastElem();
}
