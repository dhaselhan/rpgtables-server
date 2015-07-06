package com.dhaselhan.rpgtables.data;

import org.eclipse.persistence.platform.database.DatabasePlatform;

public class SqlitePlatform extends DatabasePlatform {

	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean supportsForeignKeyConstraints() {
        return false;
	}
}
