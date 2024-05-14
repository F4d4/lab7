package server.commands;

import global.facility.Response;
import global.facility.Ticket;
import server.rulers.DatabaseRuler;

import java.sql.SQLException;

import static server.tools.PasswordHashing.*;

public class Registration extends Command{
    private DatabaseRuler databaseRuler;
    public Registration(DatabaseRuler databaseRuler){
        super("login" , "добавить пользователя");
        this.databaseRuler = databaseRuler;
    }

    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password) throws SQLException {
        try {
            String salt = generateSalt();
            databaseRuler.insertUser(login, hashPasswordWithSalt(password,salt),salt );
            return new Response("Вы успешно зарегестрировались" , true);
        }catch(SQLException e){
            return new Response("Ошибка! Не удалось добавть пользователя");
        }

    }
}
