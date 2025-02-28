package net.johjoh.nexus.cloud.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer<T extends Serializable> {
	
	public boolean serialize(T serializable, File file) {
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(serializable);
			out.close();
			fos.close();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public T deserialize(File file, Class<T> clazz) {
		try {
			if(file.exists()) {
				FileInputStream fileIn = new FileInputStream(file);
			    ObjectInputStream in = new ObjectInputStream(fileIn);
			    Object o = in.readObject();
			    in.close();
			    fileIn.close();
				if(clazz.isInstance(o)) {
					return clazz.cast(o);
				}
			}
			return null;
		}
		catch(Exception e) {
			return null;
		}
	}

}
