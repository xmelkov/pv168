package cz.muni.fi.Swing;

import javax.sql.DataSource;

import static cz.muni.fi.Main.createMemoryDatabase;

/**
 * Created by Matúš on 20.5.2017.
 */
public class SetupDB {

    public static DataSource createDB() {
        return createMemoryDatabase();
    }
}
