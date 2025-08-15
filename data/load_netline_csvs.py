#!/usr/bin/env python3
"""
Script simples em Python para popular tabelas PostgreSQL a partir dos CSVs:
  - cargo.csv (id, nome)
  - vencimento.csv (id, descricao, valor, tipovencimento)
  - cargo_vencimento.csv (id, cargo_id, vencimento_id)
  - pessoa.csv (id, nome, cidade, email, cep, endereco, pais, usuario, telefone, datanascimento, cargo_id)

Requisitos:
    pip install psycopg2-binary python-dotenv pandas

Como usar:
    # Defina a URL do banco por env ou .env (no mesmo diretório do script)
    # Ex.: postgresql://usuario:senha@host:5432/banco
    export DATABASE_URL="postgresql://user:pass@localhost:5432/seu_banco"

    # ou crie um .env com:
    # DATABASE_URL=postgresql://user:pass@localhost:5432/seu_banco

    python load_netline_csvs.py --data-dir /mnt/data --schema public

Observações:
  - Delimitador dos CSVs: ';'
  - Faz UPSERT (ON CONFLICT (id) DO UPDATE), então roda quantas vezes precisar.
  - Converte 'valor' para NUMERIC e 'datanascimento' para DATE no formato MM/DD/YYYY.
  - Assume nomes de colunas/campos conforme os arquivos enviados.
"""
import argparse
import os
from typing import List
import psycopg2
from psycopg2.extras import execute_batch
from dotenv import load_dotenv
import pandas as pd

def get_conn():
    load_dotenv(override=True)
    dsn = os.environ.get("DATABASE_URL")
    if not dsn:
        raise RuntimeError("Defina DATABASE_URL no ambiente ou arquivo .env")
    return psycopg2.connect(dsn)

def read_csv(path: str) -> pd.DataFrame:
    return pd.read_csv(path, sep=';', dtype=str, keep_default_na=False, na_values=[''])

def coerce_int(series: pd.Series) -> pd.Series:
    return pd.to_numeric(series, errors='coerce').astype("Int64")

def coerce_numeric(series: pd.Series) -> pd.Series:
    return pd.to_numeric(series.str.replace(',', '.', regex=False), errors='coerce')

def coerce_date_mdy(series: pd.Series) -> pd.Series:
    return pd.to_datetime(series, format="%m/%d/%Y", errors="coerce").dt.date

def upsert_generic(cur, schema: str, table: str, rows: List[dict], unique_cols: List[str]):
    if not rows:
        print(f"[SKIP] {table}: sem linhas.")
        return
    cols = list(rows[0].keys())
    placeholders = ", ".join(["%s"] * len(cols))
    colnames = ", ".join([f'"{c}"' for c in cols])
    conflict = ", ".join([f'"{c}"' for c in unique_cols])
    set_clause = ", ".join([f'"{c}" = EXCLUDED."{c}"' for c in cols if c not in unique_cols])

    sql = f'''
        INSERT INTO "{schema}"."{table}" ({colnames})
        VALUES ({placeholders})
        ON CONFLICT ({conflict}) DO UPDATE
        SET {set_clause};
    '''

    values = [tuple(r[c] for c in cols) for r in rows]
    execute_batch(cur, sql, values, page_size=2000)
    print(f"[OK] {table}: upsert {len(rows)} linha(s).")

def load_cargo(cur, schema: str, df: pd.DataFrame):
    df['id'] = coerce_int(df['id'])
    rows = [{"id": int(i), "nome": (n if n is not None else None)} for i, n in zip(df['id'], df['nome']) if pd.notna(i)]
    upsert_generic(cur, schema, "cargo", rows, ["id"])

def load_vencimento(cur, schema: str, df: pd.DataFrame):
    df['id'] = coerce_int(df['id'])
    df['valor'] = coerce_numeric(df['valor'])
    rows = []
    for _, r in df.iterrows():
        if pd.isna(r['id']):
            continue
        rows.append({
            "id": int(r['id']),
            "descricao": r.get('descricao'),
            "valor": float(r['valor']) if pd.notna(r['valor']) else None,
            "tipovencimento": r.get('tipovencimento')
        })
    upsert_generic(cur, schema, "vencimento", rows, ["id"])

def load_cargo_vencimento(cur, schema: str, df: pd.DataFrame):
    for col in ("id","cargo_id","vencimento_id"):
        df[col] = coerce_int(df[col])
    rows = []
    for _, r in df.iterrows():
        if pd.isna(r['id']):
            continue
        rows.append({
            "id": int(r['id']),
            "cargo_id": int(r['cargo_id']) if pd.notna(r['cargo_id']) else None,
            "vencimento_id": int(r['vencimento_id']) if pd.notna(r['vencimento_id']) else None,
        })
    upsert_generic(cur, schema, "cargo_vencimento", rows, ["id"])

def load_pessoa(cur, schema: str, df: pd.DataFrame):
    for col in ("id","cargo_id"):
        df[col] = coerce_int(df[col])
    df['datanascimento'] = coerce_date_mdy(df['datanascimento'])
    rows = []
    for _, r in df.iterrows():
        if pd.isna(r['id']):
            continue
        rows.append({
            "id": int(r['id']),
            "nome": r.get('nome'),
            "cidade": r.get('cidade'),
            "email": r.get('email'),
            "cep": r.get('cep'),
            "endereco": r.get('endereco'),
            "pais": r.get('pais'),
            "usuario": r.get('usuario'),
            "telefone": r.get('telefone'),
            "datanascimento": r['datanascimento'],
            "cargo_id": int(r['cargo_id']) if pd.notna(r['cargo_id']) else None,
        })
    upsert_generic(cur, schema, "pessoa", rows, ["id"])

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--data-dir", default="/mnt/data", help="Diretório com os CSVs")
    ap.add_argument("--schema", default="public", help="Schema do PostgreSQL")
    args = ap.parse_args()

    data_dir = args.data_dir
    schema = args.schema

    cargo_csv = os.path.join(data_dir, "cargo.csv")
    vencimento_csv = os.path.join(data_dir, "vencimento.csv")
    cargo_vencimento_csv = os.path.join(data_dir, "cargo_vencimento.csv")
    pessoa_csv = os.path.join(data_dir, "pessoa.csv")

    cargo_df = read_csv(cargo_csv)
    vencimento_df = read_csv(vencimento_csv)
    cv_df = read_csv(cargo_vencimento_csv)
    pessoa_df = read_csv(pessoa_csv)

    with get_conn() as conn:
        with conn.cursor() as cur:
            load_cargo(cur, schema, cargo_df)
            load_vencimento(cur, schema, vencimento_df)
            load_cargo_vencimento(cur, schema, cv_df)
            load_pessoa(cur, schema, pessoa_df)
        conn.commit()
    print("Concluído.")

if __name__ == "__main__":
    main()
