package server.rulers;

import global.facility.Ticket;

import java.time.LocalDateTime;
import java.util.*;
/**
 * Класс Руководителя коллекцией
 */

public class CollectionRuler {
    private DatabaseRuler databaseRuler;

    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private PriorityQueue<Ticket> collection = new PriorityQueue<>();

    public CollectionRuler(DatabaseRuler databaseRuler){
        this.databaseRuler = databaseRuler;
        this.lastInitTime=null;
        this.lastSaveTime=null;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }


    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }


    public PriorityQueue<Ticket> getCollection() {
        return collection;
    }


    /**
     * Ищет элемент коллекции по id
     */
    public Ticket byId(Long id){
        for (Ticket element:collection){
            if (element.getId() == id) return element;
        }
        return null;
    }

    /**
     * Проверяет содержит ли коллекция элемент
     *
     * @return возвращает true если элемент существует в коллекции
     */
    public boolean isContain(Ticket t){
        return t == null || byId(t.getId()) != null;
    }


    /**
     * Сортировка коллекции
     */
    public void update() {
        List<Ticket> tempList = new ArrayList<>(collection);
        Collections.sort(tempList);
        collection = new PriorityQueue<>(tempList);
    }
    /**
     * Инициализация коллекции из файла
     *
     *@return возвращает сообщение о  успешности выполнения метода
     */

    public boolean init() {
        collection.clear();
        collection = databaseRuler.loadCollection();
        lastInitTime = LocalDateTime.now();
        update();
        return true;
    }

    /**
     * Добавление элемента в коллекцию
     *
     * @return возвращает сообщение о  успешности выполнения метода
     */
    public boolean add(Ticket a){
        if(isContain(a)){
            return false;
        }
        collection.add(a);
        update();
        return true;
    }
    /**
     * Удаляет элемент из коллекции
     */
    public void remove(Ticket t){
        collection.remove(t);
    }
    /**
     * Удаляет все элементы в коллекции
     */
    public void removeAll(){
        collection.clear();
    }
    /**
     * удаляет первый элемент коллекции
     */
    public void removefirst(){
        collection.poll();
    }

    public boolean collectionIsEmpty(){
        return collection.isEmpty();
    }

    public Ticket getFirstTicketToRemove(){
        return collection.peek();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (var Ticket : collection) {
            info.append(Ticket+"\n\n");
        }
        return info.toString().trim();
    }



}
