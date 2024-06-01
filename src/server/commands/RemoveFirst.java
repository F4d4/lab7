package server.commands;


import global.facility.Response;
import global.facility.Ticket;
import server.rulers.CollectionRuler;
import server.rulers.DatabaseRuler;

import javax.xml.crypto.Data;
import java.sql.SQLException;

/**
 * команда удаляющая первый элемент  коллекции
 */
public class RemoveFirst extends Command{
    private final CollectionRuler collectionRuler;
    private final DatabaseRuler databaseRuler;


    public RemoveFirst(CollectionRuler collectionRuler , DatabaseRuler databaseRuler){
        super("remove_first", "удалить первый элемент коллекции");

        this.collectionRuler=collectionRuler;
        this.databaseRuler = databaseRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        if(!arguments[1].isEmpty()){
            return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'");
        }

        if(!collectionRuler.collectionIsEmpty()){
            try {
                var deletable= collectionRuler.getFirstTicketToRemove();
                var userID = collectionRuler.getUserid(login);
                var deletableId = deletable.getId();
                var checkingUserID =  collectionRuler.isCorrectID(deletableId);
                if(userID==checkingUserID){
                    collectionRuler.removefirst();
                    return new Response("Ticket удалён");
                }else {
                    return new Response("Ticket не удален! Возможные причины: вы попытались удалить чужой ticket");
                }
            }catch (SQLException e){
                return new Response("Ошибка удаления ticket в базе данных");
            }
        }else {
            return new Response("Невозможно удалить элемент , так как коллекция пуста");
        }
    }
}
