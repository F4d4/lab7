package server.commands;

import client.tools.Ask;
import global.facility.Response;
import global.facility.Ticket;
import server.rulers.CollectionRuler;
import server.rulers.DatabaseRuler;

import java.sql.SQLException;

/**
 * Добавление элемента , если параметр price меньше минимального price в коллекции
 */
public class AddIfMin extends Command {
    private final CollectionRuler collectionRuler;
    private final DatabaseRuler databaseRuler;

    public AddIfMin( CollectionRuler collectionRuler , DatabaseRuler databaseRuler){
        super("add_if_min" , "добавить новый элемент в коллекцию , если его значение меньше чем у наименьшего элемента коллекции");

        this.collectionRuler = collectionRuler;
        this.databaseRuler = databaseRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply (String[] arguments , Ticket ticket,String login,String password){
        try{
            if(!arguments[1].isEmpty()){
                return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
            }

            ticket.setUser_id(databaseRuler.getUserID(login));
            var minPrice = minPrice();
            if(ticket.getPrice()<minPrice){
                if(ticket!= null&&ticket.validate()){
                    databaseRuler.insertTicket(ticket);
                    collectionRuler.init();
                }else{
                    return new Response("Поля Ticket не валидны! Ticket не создан!");
                }
            }else{
                return new Response("Продукт не добавлен, цена не минимальная (" + ticket.getPrice() + " >= " + minPrice +")");
            }
            collectionRuler.update();
            return new Response("Ticket добавлен!");
        }catch (SQLException e ){
            return new Response("Ошибка при добавлении билета");
        } catch (Exception e) {
            return new Response("Непредвиденная ошибка при добавлении билета");
        }

    }

    private Long minPrice() {
        return collectionRuler.getCollection().stream()
                .map(Ticket::getPrice)
                .mapToLong(Long::longValue)
                .min()
                .orElse(Long.MAX_VALUE);
    }
}
