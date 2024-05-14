package server.commands;

import global.facility.Response;
import global.facility.Ticket;

import java.sql.SQLException;

/**
 * Интерфейс для всех комманд
 */
public interface Executable {
    Response apply(String[] arguments , Ticket ticket , String login , String password) throws SQLException;
}
