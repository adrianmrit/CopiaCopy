package languages;

import java.util.ListResourceBundle;

public class ESBundle extends ExtendedListResourceBundle{
	protected Object[][] getErrorMessages(){return null;};
	
	@Override
	protected Object[][] getContents() {
		return new Object[][] {
			{"file", "archivo"},
			{"files", "archivos"},
			{"folder", "carpeta"},
			{"folders", "carpetas"},
			{"shortcut", "acceso directo"},
			{"shortcuts", "accesos directos"},
			{"symbolic link", "enlace symbólico"},
			{"symbolic links", "enlaces symbólicos"},
			{"from", "Desde"},
			{"to", "Hasta"},
			{"origin", "Origen"},
			{"destination", "Destino"},
			{"name", "Nombre"},
			{"error", "Error"},
			{"rename", "Renombrar"},
			{"size", "Tamaño"},
			{"pause", "Pausar"},
			{"resume", "Resumir"},
			{"cancel", "Cancelar"},
			{"skip", "Saltar"},
			{"skip selected", "Saltar archivos"},
			{"merge", "Fucionar"},
			{"replace", "Reemplazar"},
			{"more", "Mostrar más"},
			{"calculating", "calculando"},
			{"replace with", "Reemplazar con:"},
			{"original", "Original:"},
			{"last modified", "Última modificación:"},
			{"do for all", "Hacerlo para todo"},
			{"renameToggle", "<HTML><p><b>Renombrar el destino</b></p></HTML>"},
			{"queueTab", "Lista de copia"},
			{"errorsTab", "Errores"},
			
			{"existsFileWindowsTitle", "Fucionar carpeta?"},
			{"existsShortcutWindowsTitle", "Fucionar carpeta?"},
			{"existsFolderWindowsTitle", "Fucionar carpeta?"},
			
			{"FileNotFoundError", "<HTML>El archivo <i>%s</i> no se pudo encontrar. Intentar de nuevo?</HTML>"},
			{"FolderAccessError", "<HTML>La carpeta <i>%s</i> no se pudo acceder. Intentar de nuevo?</HTML>"},
			{"ReadPermissionError", "<HTML>Parece que no tienes permiso para escribir el archivo <i>%s</i>. Intentar de nuevo?</HTML>"},
			{"WriteError", "<HTML>Hubo un error mientras se escribía el archivo <i>%s</i>. Intentar de nuevo?</HTML>"},
			{"WritePermissionError", "<HTML>Parece que no tienes permiso para leer el archivo <i>%s</i>. Intentar de nuevo?</HTML>"},
			{"ExistButItsFolderError", "<HTML> <i>%s</i> existe pero es una carpeta. Será ignorarado.</HTML>"},
			{"ExistButItsFileError", "<HTML> <i>%s</i> existe pero es un archivo. Será ignorarado.</HTML>"},
			{"ExistButItsShortcutError", "<HTML> <i>%s</i> existe pero es un acceso directo. Será ignorarado.</HTML>"},
			{"CopyError", "Error en la copia"},
			{"CopyIntoItselfError", "No puedes copiar una carpeta dentro de ella misma"},
			
			{"mergeFolderTitleFormat", "Fucionar carpeta %s?"},
			{"replaceFileTitleFormat", "Reemplazar archivo %s?"},
			{"replaceShortcutTitleFormat", "Reemplazar acceso directo %s?"},
			
			{"originFormat", "origen: %s"},
			{"destinationFormat", "destino: %s"},
			{"containsFormat", "Contiene %d elementos%s"},
			{"sizeFormat", "Tamaño: %.2f%s"},
			{"infoFormat", "%d/%d objetos - %s/%s - %s - %s"},
			{"loadingFormat", "Cargando %s objetos"},
			{"invalidNameFormat", "%s no es un nombre válido"},
			{"existsFileFormat", "Un archivo con el mismo nombre existe en %s, reemplazar va a sobreescribir su contenido."},
			{"existsShortcutFormat", "Un acceso directo con el mismo nombre existe en %s, reemplazar va a sobreescribir su contenido."},
			{"existsFolderFormat", "Una carpeta con el mismo nombre existe en %s, fucionar conenido?"},
			
			{"copyIntoItselfException", "Intento de copia recursiva"},
			{"nameConflictException", "Conflicto en el nombre"},
			{"pathNotFoundException", "No se puede encontrar"},
			{"permissionException", "Permisos insuficientes"},
			{"writeException", "Error intentando escribir el destino"},
			
			{"settingsTitle", "Ajustes"},
			
			{"langPickerLabel", "Lenguaje de la interfaz:"},
			{"themePickerLabel", "Tema:"},
			
			{"langTab", "Lenguajes"},
			{"lookAndFeelTab", "Interface"},
		};
	}

}
