/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Archivos;


import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import javax.swing.JOptionPane;
import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipRAR {

    /**
     * este metodo  comprime archivos
     * @param filePath
     * @param zipFilePath 
     */
    public static void comprimirArchivo(String filePath, String zipFilePath) {
        File fileToZip = new File(filePath);
        if (!fileToZip.exists()) {
            System.err.println("El archivo a comprimir no existe: " + filePath);
            return;
        }
        if (!zipFilePath.toLowerCase().endsWith(".zip")) {
            zipFilePath += ".zip";
        }
        try (FileInputStream fis = new FileInputStream(fileToZip);
             FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zos.putNextEntry(new ZipEntry(fileToZip.getName()));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            System.out.println("Archivo comprimido " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Error al comprimir el archivo: " + e.getMessage());
        }
    }
/**
 * este comprime directorios
 * @param dirPath
 * @param zipFilePath 
 */
    public static void comprimirDirectorio(String dirPath, String zipFilePath) {
        File dirToZip = new File(dirPath);
        if (!dirToZip.exists() || !dirToZip.isDirectory()) {
            System.err.println("El directorio a comprimir no existe" + dirPath);
            return;
        }
        if (!zipFilePath.toLowerCase().endsWith(".zip")) {
            zipFilePath += ".zip";
        }
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            comprimirDirectorioRecursivamente(dirToZip, dirToZip.getName(), zos);
            System.out.println("Directorio comprimido: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Error al comprimir el directorio: " + e.getMessage());
        }
    }
    /**
     * este comprime directorios
     * @param fileToZip
     * @param fileName
     * @param zos
     * @throws IOException 
     */
    private static void comprimirDirectorioRecursivamente(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (!fileName.endsWith("/")) {
                fileName += "/";
            }
            zos.putNextEntry(new ZipEntry(fileName));
            zos.closeEntry();
            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    comprimirDirectorioRecursivamente(childFile, fileName + childFile.getName(), zos);
                }
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            zos.putNextEntry(new ZipEntry(fileName));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
        }
    }
  /**
   * este metodod descomprime archivos
   * @param zipFilePath
   * @param destDir 
   */
    public static void descomprimirArchivo(String zipFilePath, String destDir) {
        Path zipPath = Paths.get(zipFilePath);
        if (!Files.exists(zipPath)) {
            System.err.println("El archivo ZIP no existe: " + zipFilePath);
            return;
        }
        Path destPath = Paths.get(destDir);
        try {
            if (!Files.exists(destPath)) {
                Files.createDirectories(destPath);
            }
            try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    Path newFilePath = destPath.resolve(zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        Files.createDirectories(newFilePath);
                    } else {
                        Files.createDirectories(newFilePath.getParent());
                        try (FileOutputStream fos = new FileOutputStream(newFilePath.toFile())) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                            }
                        }
                    }
                    zis.closeEntry();
                    zipEntry = zis.getNextEntry();
                }
                System.out.println("Archivo descomprimido : " + destDir);
            }
        } catch (IOException e) {
            System.err.println("Error al descomprimir el archivo: " + e.getMessage());
        }
    }

    /**
     * este metodo comprime los archivos con la contraseña
     * @param filePath
     * @param zipFilePath
     * @param password 
     */
public static void zipFileWithPassword(String filePath, String zipFilePath, String password) {
        File fileToZip = new File(filePath);
        if (!fileToZip.exists()) {
            System.err.println("El archivo a comprimir no existe: " + filePath);
            return;
        }

        try {
            ZipFile zipFile = new ZipFile(zipFilePath, password.toCharArray());
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
            zipFile.addFile(fileToZip, zipParameters);
            System.out.println("Archivo comprimido exitosamente con contraseña: " + zipFilePath);
        } catch (ZipException e) {
            System.err.println("Error al comprimir el archivo con contraseña: " + e.getMessage());
        }
    }
   /**
    * este descomprime archivos con contraseña
    * @param zipFilePath
    * @param destDir
    * @param password 
    */
    public static void descomprimirArchivoConContraseña(String zipFilePath, String destDir, String password) {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            System.err.println("El archivo ZIP no existe: " + zipFilePath);
            return;
        }
        File destDirectory = new File(destDir);
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }
        try {
            ZipFile zipFileWithPassword = new ZipFile(zipFilePath);
            if (zipFileWithPassword.isEncrypted()) {
                zipFileWithPassword.setPassword(password.toCharArray());
            }
            zipFileWithPassword.extractAll(destDir);
            System.out.println("Archivo descomprimido exitosamente en: " + destDir);
        } catch (ZipException e) {
            System.err.println("Error al descomprimir el archivo con contraseña: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Bienvenido al programa de compresión y descompresión de archivos!");

        while (true) {
            String[] options = {
                "Comprimir archivo",
                "Comprimir directorio",
                "Descomprimir archivo",
                "Comprimir archivo con contraseña",
                "Descomprimir archivo con contraseña",
                "Salir"
            };

            String message = "Seleccione una opción:";
            int option = JOptionPane.showOptionDialog(null, message,
                    "Menú",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            switch (option) {
                case 0: 
                    String filePath = JOptionPane.showInputDialog("Ingrese la ruta del archivo a comprimir:");
                    String zipFilePath = JOptionPane.showInputDialog("Ingrese la ruta del archivo ZIP de salida:");
                    ZipRAR.comprimirArchivo(filePath, zipFilePath);
                    break;
                case 1: 
                    String dirPath = JOptionPane.showInputDialog("Ingrese la ruta del directorio a comprimir:");
                    zipFilePath = JOptionPane.showInputDialog("Ingrese la ruta del archivo ZIP de salida:");
                    ZipRAR.comprimirDirectorio(dirPath, zipFilePath);
                    break;
                case 2: 
                    zipFilePath = JOptionPane.showInputDialog("Ingrese la ruta del archivo ZIP a descomprimir:");
                    String destDir = JOptionPane.showInputDialog("Ingrese la ruta del directorio donde se va a copiar:");
                    ZipRAR.descomprimirArchivo(zipFilePath, destDir);
                    break;
                case 3: 
                    filePath = JOptionPane.showInputDialog("Ingrese la ruta del archivo a comprimir:");
                    zipFilePath = JOptionPane.showInputDialog("Ingrese la ruta del archivo ZIP de salida:");
                    String password = JOptionPane.showInputDialog("Ingrese la contraseña para el archivo comprimido:");
                    ZipRAR.zipFileWithPassword(filePath, zipFilePath, password);
                    break;
                case 4: 
                    zipFilePath = JOptionPane.showInputDialog("Ingrese la ruta del archivo ZIP a descomprimir:");
                    destDir = JOptionPane.showInputDialog("Ingrese la ruta del directorio donde se va a copiar:");
                    password = JOptionPane.showInputDialog("Ingrese la contraseña para el archivo ZIP:");
                    ZipRAR.descomprimirArchivoConContraseña(zipFilePath, destDir, password);
                    break;
                case 5: 
                    JOptionPane.showMessageDialog(null, "¡Adiós!");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }

}

