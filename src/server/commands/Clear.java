package server.commands;

import global.facility.Response;
import global.facility.Ticket;
import server.rulers.CollectionRuler;

import java.sql.SQLException;

/**
 * команда очищает коллекцию
 */
public class Clear extends Command {
    private final CollectionRuler collectionRuler;

    public Clear( CollectionRuler collectionRuler) {
        super("clear", "очистить коллекцию");
        this.collectionRuler = collectionRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply (String[] arguments , Ticket ticket,String login,String password){
        if(!arguments[1].isEmpty()){
            return new Response("Неправильное количество аргументов!\nИспользование: '\" + getName() + \"'");
        }

        try{
            if(!(collectionRuler.getCollection().size()==0)){
                var userID = collectionRuler.getUserid(login);
                collectionRuler.removeAll(userID);
                return new Response("коллекция очищена");
            }else{
                return new Response("коллекция пуста");
            }
        }catch (SQLException e){
            return new Response("Ошибка в базе данных во время очищения коллекции пользователя");
        }
    }
}
