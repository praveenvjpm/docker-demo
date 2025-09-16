import pandas as pd
from pathlib import Path

def resolve_keys(columns, keys):
    """Resolve key names case-insensitively to actual column names."""
    lower_map = {c.lower(): c for c in columns}
    resolved = []
    for k in keys:
        lk = k.lower()
        if lk not in lower_map:
            raise ValueError(f"Key column '{k}' not found in data; available columns: {list(columns)}")
        resolved.append(lower_map[lk])
    return resolved

def consolidate_csvs():
    # Hardcoded paths - replace with your actual paths
    input_dir = '/path/to/your/csv_folder'  # Folder containing CSV files
    output_csv = '/path/to/consolidated.csv'  # Output consolidated CSV path

    input_path = Path(input_dir)
    csv_files = sorted(input_path.glob("*.csv"))
    if not csv_files:
        raise FileNotFoundError(f"No CSV files found in: {input_dir}")

    # Read and concatenate all CSVs
    frames = [pd.read_csv(f) for f in csv_files]
    combined = pd.concat(frames, ignore_index=True)

    # Define keys (you can change these if needed)
    keys = ('userid', 'orderid')
    resolved_keys = resolve_keys(combined.columns, keys)

    # Identify columns to sum (all non-key columns)
    sum_cols = [c for c in combined.columns if c not in resolved_keys]

    # Convert non-key columns to numeric; coerce errors to NaN, then fill NaN/blanks with 0.00
    for c in sum_cols:
        combined[c] = pd.to_numeric(combined[c], errors='coerce').fillna(0.00)

    # Group by keys and sum the other columns
    if sum_cols:
        consolidated = combined.groupby(resolved_keys, as_index=False)[sum_cols].sum()
        # Round summed values to two decimal places for precision (e.g., 0.00)
        consolidated[sum_cols] = consolidated[sum_cols].round(2)
    else:
        # If no other columns, just deduplicate keys
        consolidated = combined.drop_duplicates(resolved_keys)

    # Save the consolidated result
    Path(output_csv).parent.mkdir(parents=True, exist_ok=True)
    consolidated.to_csv(output_csv, index=False)
    print(f"Consolidated CSV saved to: {output_csv}")

if __name__ == "__main__":
    consolidate_csvs()
