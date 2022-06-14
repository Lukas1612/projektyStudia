import sqlite3
from sqlite3 import Error

class DatebaseManager:
    def __init__(self, db_file):
        self.db_file = db_file
        self.init_tables()
        self.init_categories()

    def init_tables(self):
        self.create_connection()

        sql_create_operations_table = """ CREATE TABLE IF NOT EXISTS operations (
                                                     id integer PRIMARY KEY,
                                                     type text NOT NULL,
                                                     amount real NOT NULL,
                                                     id_category integer NOT NULL,
                                                     date int NOT NULL,
                                                     dateText text NOT NULL,
                                                     note text,
                                                     FOREIGN KEY (id_category) REFERENCES categories (id)
                                                 ); """

        sql_create_categories_table = """ CREATE TABLE IF NOT EXISTS categories (
                                                             id integer PRIMARY KEY,
                                                             name text NOT NULL UNIQUE
                                                         ); """
        conn = self.create_connection()

        if conn is not None:
            # create projects table
            self.create_table(conn, sql_create_operations_table)
            self.create_table(conn, sql_create_categories_table)
        else:
            print("Error! cannot create the database connection.")

    def init_categories(self):

        rows = self.select_all_from_categories()

        noRowsHere = 1

        for row in rows:
            noRowsHere = 0


        if noRowsHere:
            category = ('Jedzenie',)
            self.insert_to_categories(category)
            category = ('Transport',)
            self.insert_to_categories(category)
            category = ('Dom',)
            self.insert_to_categories(category)
            category = ('Ubrania',)
            self.insert_to_categories(category)
            category = ('Rozrywka',)
            self.insert_to_categories(category)
            category = ('Zdrowie',)
            self.insert_to_categories(category)
            category = ('Edukacja',)
            self.insert_to_categories(category)
            category = ('Inne',)
            self.insert_to_categories(category)

    def insert_to_operations(self, operation):
        sql = ''' INSERT INTO operations(type, amount, id_category, date, dateText,  note)
                     VALUES(?,?,?,?,?,?) '''
        conn = self.create_connection()

        cur = conn.cursor()
        cur.execute(sql, operation)
        conn.commit()

    def insert_to_categories(self, category):
        sql = ''' INSERT INTO categories(name)
                     VALUES(?) '''
        conn = self.create_connection()

        cur = conn.cursor()
        cur.execute(sql, category)
        conn.commit()

        return cur.lastrowid

    def select_all_from_categories(self):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute("SELECT * FROM categories")

        rows = cur.fetchall()
        return rows

    def select_from_categories_where_name_is(self, name):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute("SELECT * FROM categories WHERE name = ?", (name,))

        rows = cur.fetchall()
        return rows
    def select_from_categories_where_id_is(self, id):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute("SELECT * FROM categories WHERE id = ?", (id,))

        rows = cur.fetchall()
        return rows

    def select_all_from_operations_where_category_id_is(self, id):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute(
            "SELECT o.type, o.amount, o.id_category, c.name, o.date, o.dateText, o.note FROM operations o JOIN categories c on c.id = o.id_category WHERE c.id = ?",
            (id,))

        rows = cur.fetchall()
        return rows

    def select_all_from_operations(self):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute("SELECT o.type, o.amount, o.id_category, c.name, o.date, o.dateText, o.note FROM operations o JOIN categories c on c.id = o.id_category ")

        rows = cur.fetchall()
        return rows

    def select_amount_from_operations(self):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute(
            "SELECT amount FROM operations")

        rows = cur.fetchall()
        return rows

    def select_all_from_operations_in_set_date(self, dateMin, dateMax):
        dateMin = int(dateMin)
        dateMax = int(dateMax)
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute(
            "SELECT o.type, o.amount, o.id_category, c.name, o.date, o.dateText, o.note FROM operations o JOIN categories c on c.id = o.id_category WHERE o.date >= ? AND o.date <= ?", (dateMin, dateMax,))

        rows = cur.fetchall()
        return rows

    def select_latest_amount_from_operations(self):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute("SELECT amount FROM operations WHERE date = (SELECT MAX(date) FROM operations)")

        rows = cur.fetchall()
        return rows
    def select_date_of_latest_operation(self):
        conn = self.create_connection()
        cur = conn.cursor()
        cur.execute("SELECT date FROM operations WHERE date = (SELECT MAX(date) FROM operations)")

        rows = cur.fetchall()
        return rows


    def create_connection(self):
        """ create a database connection to the SQLite database
            specified by db_file
        :param db_file: database file
        :return: Connection object or None
        """
        conn = None
        try:
            conn = sqlite3.connect(self.db_file)
            return conn
        except Error as e:
            print(e)

        return conn

    def create_table(self, conn, create_table_sql):
        """ create a table from the create_table_sql statement
        :param conn: Connection object
        :param create_table_sql: a CREATE TABLE statement
        :return:
        """
        try:
            c = conn.cursor()
            c.execute(create_table_sql)
        except Error as e:
            print(e)
