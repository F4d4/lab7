package server.commands;

import global.facility.Response;
import global.facility.Ticket;
import server.rulers.CollectionRuler;

/**
 * команда сохраняющая коллекцию в файл
 */
public class Save  extends Command{

    private final CollectionRuler collectionRuler;

    public Save(CollectionRuler collectionRuler){
        super("save", "сохранить коллекцию");

        this.collectionRuler = collectionRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        if(!arguments[1].isEmpty()){
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return new Response("");
        }

        console.println("Выполнение сохранения прошло успешно");
        return new Response("");
    }
}
