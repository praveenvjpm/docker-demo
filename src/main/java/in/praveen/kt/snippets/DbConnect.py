import pandas as pd
import pyodbc

def connect_to_mssql(server, database, username, password):
    conn_str = (
        f"DRIVER={{ODBC Driver 17 for SQL Server}};"
        f"SERVER={server};"
        f"DATABASE={database};"
        f"UID={username};"
        f"PWD={password}"
    )
    conn = pyodbc.connect(conn_str)
    return conn

def insert_excel_to_mssql(excel_path, table_name, conn):
    # Read Excel file, first row as columns
    df = pd.read_excel(excel_path)

    cursor = conn.cursor()

    # Compose insert statement
    columns = df.columns.tolist()
    cols_str = ', '.join(f'[{col}]' for col in columns)
    placeholders = ', '.join('?' for _ in columns)
    insert_sql = f"INSERT INTO {table_name} ({cols_str}) VALUES ({placeholders})"

    # Insert rows
    for _, row in df.iterrows():
        values = tuple(row[col] for col in columns)
        cursor.execute(insert_sql, values)

    conn.commit()
    cursor.close()

# Usage example - fill in your details here
server = 'your_server_name'
database = 'your_database_name'
username = 'your_username'
password = 'your_password'
excel_file_path = 'path_to_your_file.xlsx'
table_name = 'your_table_name'

conn = connect_to_mssql(server, database, username, password)
insert_excel_to_mssql(excel_file_path, table_name, conn)
conn.close()
