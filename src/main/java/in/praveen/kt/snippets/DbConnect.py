import pandas as pd
import pyodbc

def insert_excel_to_mssql(excel_path, table_name, conn_str):
    # Read Excel file (first row as columns)
    df = pd.read_excel(excel_path)

    # Connect to MSSQL
    conn = pyodbc.connect(conn_str)
    cursor = conn.cursor()

    # Prepare insert query dynamically based on columns
    columns = df.columns.tolist()
    cols_str = ', '.join(f"[{col}]" for col in columns)
    placeholders = ', '.join('?' for _ in columns)
    insert_sql = f"INSERT INTO {table_name} ({cols_str}) VALUES ({placeholders})"

    # Insert each row of data
    for _, row in df.iterrows():
        values = tuple(row[col] for col in columns)
        cursor.execute(insert_sql, values)

    conn.commit()
    cursor.close()
    conn.close()

# Example connection string pattern:
# conn_str = 'DRIVER={ODBC Driver 17 for SQL Server};SERVER=server_name;DATABASE=db_name;UID=user;PWD=password'

# Use like:
# insert_excel_to_mssql('data.xlsx', 'MyTable', conn_str)
