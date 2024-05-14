package server.commands;

import global.facility.Response;
import global.facility.Ticket;
import server.rulers.CollectionRuler;
import global.exeptions.NotFoundException;
import server.rulers.DatabaseRuler;

import javax.xml.crypto.Data;
import java.sql.SQLException;

/**
 * команда удаляющая элемент из коллекции по его id
 */
public class RemoveById extends Command {
    private final CollectionRuler collectionRuler;
    private final DatabaseRuler databaseRuler;


    public RemoveById(CollectionRuler collectionRuler , DatabaseRuler databaseRuler) {
        super("remove_by_id", "удалить элемент из коллекции по его id");

        this.collectionRuler = collectionRuler;
        this.databaseRuler = databaseRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        if(arguments[1].isEmpty()){
            return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
        }
        try{
            long deletableId= Long.parseLong(arguments[1]);
            var deletable= collectionRuler.byId(deletableId);
            if (deletable == null) throw new NotFoundException();
            var userID = databaseRuler.getUserID(login);
            var checkingUserID = databaseRuler.isCorrectID(deletableId);
            if(userID==checkingUserID){
                databaseRuler.removeTicketById(deletableId);
                collectionRuler.remove(deletable);
                return new Response("Ticket удалён");
            }else{
                return new Response("Ticket не удален! Возможные причины: вы попытались удалить чужой ticket");
            }
        }catch(NotFoundException e){
            return new Response("Продукта с таким ID в коллекции нет!");
        }catch (SQLException e){
            return new Response("Ошибка в базе данных при удалении ticket по его  id");
        }
    }
}
