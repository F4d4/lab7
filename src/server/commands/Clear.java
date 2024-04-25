package server.commands;

import global.facility.Response;
import global.facility.Ticket;
import server.rulers.CollectionRuler;

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
    public Response apply (String[] arguments , Ticket ticket){
        if(!arguments[1].isEmpty()){
            //console.println("Неправильное количество аргументов!");
            //console.println("Использование: '" + getName() + "'");
            return new Response("Неправильное количество аргументов!\nИспользование: '\" + getName() + \"'");
        }

        if(!(collectionRuler==null)){
            collectionRuler.removeAll();
            //console.println("коллекция очищена");
            return new Response("коллекция очищена");
        }else{
            //console.println("коллекция пуста");
            return new Response("коллекция пуста");
        }
    }
}
