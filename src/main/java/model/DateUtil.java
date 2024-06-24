package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Clase de utilidades para manejar conversiones de fechas utilizando la clase {LocalDate}.
 * Ofrece métodos estáticos para parsear y formatear fechas en formato ISO_LOCAL_DATE ('yyyy-MM-dd').
 */

public class DateUtil {
	/**
     * Formateador de fecha en formato ISO_LOCAL_DATE, utilizado para convertir entre String y LocalDate.
     */
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    /**
     * Convierte una cadena de texto que representa una fecha en formato 'yyyy-MM-dd' a un objeto {@link LocalDate}.
     *
     * @param dateString La cadena de texto de la fecha a convertir.
     * @return Un objeto {@link LocalDate} que representa la fecha.
     * @throws IllegalArgumentException si el formato de la fecha no es válido.
     */
    public static LocalDate parse(String dateString) {
        try {
            return LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.", e);
        }
    }
    /**
     * Formatea un objeto {LocalDate} a una cadena de texto en formato 'yyyy-MM-dd'.
     *
     * @param date La fecha de tipo {LocalDate} a formatear.
     * @return La cadena de texto de la fecha formateada.
     */
    public static String format(LocalDate date) {
        return date.format(dateFormatter);
    }
}