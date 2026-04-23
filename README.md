# High-Load Catalog

## Overview

This service solves performance and scalability issues in a product catalog system.

It focuses on efficient querying, caching, and minimizing database load under high traffic.

---

## Tech Stack

- Language: Java 21
- Framework: Spring Boot 3
- ORM: Spring Data JPA (Hibernate)
- Database: PostgreSQL
- Cache: Redis
- Build Tool: Maven
- Containerization: Docker (PostgreSQL + Redis)

---

## What Problems Are Solved

### 1. Slow Pagination

Problem:  
OFFSET-based pagination degrades as data grows.

Solution:  
Cursor-based pagination:

WHERE id < :lastId  
ORDER BY id DESC  
LIMIT :limit

---

### 2. Inefficient Search

Problem:  
Case-insensitive search can be slow and error-prone.

Solution:

- PostgreSQL `ILIKE`
- Safe parameter handling with explicit casting:

p.name ILIKE ('%' || cast(:search as text) || '%')

---

### 3. Missing Indexes (Critical)

Problem:  
Queries degrade to full table scans without proper indexing.

Solution:

CREATE INDEX idx_products_category_id ON products(category_id);  
CREATE INDEX idx_products_id_desc ON products(id DESC);  
CREATE INDEX idx_products_name_lower ON products (LOWER(name));

---

### 4. Heavy Aggregation

Problem:  
Calculating derived data in application layer increases load.

Solution:

Aggregation is handled in SQL:

SELECT MIN(pv.price)

---

### 5. Database Overload

Problem:  
Repeated identical queries increase database pressure.

Solution:

Redis caching:

- Cache key based on filters (category + search)
- Cache only first page
- TTL: 10 minutes
- Cache invalidation on write operations

---

### 6. Category Lookup Overhead

Problem:  
Repeated category fetch increases query count.

Solution:

In-memory cache:

- Loaded on startup
- O(1) lookup by id

---

## Key Design Decisions

- Cursor pagination instead of OFFSET
- SQL-first optimization
- Proper indexing strategy
- Redis for hot data
- Simple and predictable API

---

## Result

- Fast queries under load
- Reduced database pressure
- Scalable read performance
- Maintainable backend structure