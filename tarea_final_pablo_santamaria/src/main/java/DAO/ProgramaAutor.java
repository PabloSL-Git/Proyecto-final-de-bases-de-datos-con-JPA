package DAO;

import modelos.entidades.Autor;

import java.util.List;
import java.util.Scanner;

public class ProgramaAutor {

    private static final AutorDAO dao = new AutorDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n------- GESTIÓN DE AUTORES -------");
            System.out.println("1. Ver todos los autores");
            System.out.println("2. Buscar autor por ID");
            System.out.println("3. Insertar autor");
            System.out.println("4. Modificar autor");
            System.out.println("5. Borrar autor");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(sc.nextLine());
            System.out.println("\n");

            switch (opcion) {
                case 1 -> listarTodos();
                case 2 -> buscarPorId();
                case 3 -> insertar();
                case 4 -> modificar();
                case 5 -> borrar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void listarTodos() {
        List<Autor> autores = dao.obtenerTodos();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(a -> System.out.println(
                    a.getIdAutor() + " | " + a.getNombre() + " " +
                            a.getApellido1() + " | " + a.getNacionalidad()));
        }
    }

    private static void buscarPorId() {
        System.out.print("ID del autor: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            Autor a = dao.buscarPorId(id);
            if (a != null)
                System.out.println(
                        a.getIdAutor() + " | " + a.getNombre() + " " +
                                a.getApellido1() + " " + (a.getApellido2() != null ? a.getApellido2() : "") +
                                " | " + a.getNacionalidad());
            else
                System.out.println("Autor no encontrado.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void insertar() {
        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido 1: ");
        String ap1 = sc.nextLine();
        System.out.print("Apellido 2 (enter para omitir): ");
        String ap2 = sc.nextLine();
        System.out.print("Nacionalidad: ");
        String nac = sc.nextLine();
        try {
            dao.insertar(new Autor(id, nombre, ap1, ap2.isEmpty() ? null : ap2, nac));
            System.out.println("Autor insertado.");
        } catch (RuntimeException e) {
            System.out.println("Error al insertar autor: " + e.getMessage());
        }
    }

    private static void modificar() {
        System.out.print("ID del autor a modificar: ");
        int id = Integer.parseInt(sc.nextLine());
        Autor existente = dao.buscarPorId(id);
        if (existente == null) {
            System.out.println("Autor no encontrado.");
            return;
        }
        System.out.print("Nuevo nombre (" + existente.getNombre() + "): ");
        String nombre = sc.nextLine();
        System.out.print("Nuevo apellido 1 (" + existente.getApellido1() + "): ");
        String ap1 = sc.nextLine();
        System.out.print("Nuevo apellido 2 (" + existente.getApellido2() + "): ");
        String ap2 = sc.nextLine();
        System.out.print("Nueva nacionalidad (" + existente.getNacionalidad() + "): ");
        String nac = sc.nextLine();
        try {
            dao.modificar(new Autor(
                    id,
                    nombre.isEmpty() ? existente.getNombre() : nombre,
                    ap1.isEmpty() ? existente.getApellido1() : ap1,
                    ap2.isEmpty() ? existente.getApellido2() : ap2,
                    nac.isEmpty() ? existente.getNacionalidad() : nac));
            System.out.println("Autor modificado.");
        } catch (RuntimeException e) {
            System.out.println("Error al modificar autor: " + e.getMessage());
        }
    }

    private static void borrar() {
        System.out.print("ID del autor a borrar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            dao.borrar(id);
            System.out.println("Autor borrado.");
        } catch (RuntimeException e) {
            System.out.println("Error al borrar autor: " + e.getMessage());
        }
    }
}