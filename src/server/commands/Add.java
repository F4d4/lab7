package server.commands;

import client.tools.Ask;
import global.facility.Response;
import global.facility.Ticket;
import server.rulers.CollectionRuler;
import server.rulers.DatabaseRuler;

import java.sql.SQLException;


/**
 * добавление элемента в коллекцию
 */

public class Add extends Command{
    private final CollectionRuler collectionRuler;
    private final DatabaseRuler databaseRuler;

    public Add( CollectionRuler collectionRuler , DatabaseRuler databaseRuler){
        super("add", "добавить новый элемент в коллекцию");
        this.collectionRuler=collectionRuler;
        this.databaseRuler = databaseRuler;
    }

    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */

    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        try{
            if(!arguments[1].isEmpty()){
                return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
            }
            ticket.setUser_id(databaseRuler.getUserID(login));
            if(ticket!= null&&ticket.validate()){
                databaseRuler.insertTicket(ticket);
                collectionRuler.init();
                collectionRuler.update();
                return new Response("Ticket добавлен!");
            }else{
                return new Response("Поля Ticket не валидны! Ticket не создан!");
            }
        }catch (SQLException e ){
            return new Response("Ошибка при добавлении билета");
        } catch (Exception e) {
            return new Response("Непредвиденная ошибка при добавлении билета");
        }
    }
}
