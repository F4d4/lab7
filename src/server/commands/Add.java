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

    public Add( CollectionRuler collectionRuler){
        super("add", "добавить новый элемент в коллекцию");
        this.collectionRuler=collectionRuler;
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

            ticket.setLogin(login);
            ticket.setUser_id(collectionRuler.getUserid(login));
            if(ticket!= null&&ticket.validate()){
                collectionRuler.addTOcollection(ticket , login);
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
