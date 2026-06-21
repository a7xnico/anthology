# 🎵 Anthology API

API REST para la gestión de canciones, álbumes y versiones musicales, con autenticación JWT y control de roles.

---

## 🚀 Descripción

Anthology API permite:

* Gestionar usuarios
* Crear y administrar álbumes
* Registrar y buscar canciones
* Subir y moderar versiones de canciones (partituras, tabs, etc.)
* Controlar acceso mediante roles (ADMIN, ARTIST)

---

## ⚙️ Configuración

Definir variables en tu cliente HTTP (ej: Postman):

* `baseUrl` → URL base del backend
  Ejemplo: `http://localhost:8080`
* `token` → JWT obtenido luego del login

---

## 🔐 Autenticación

### Registro de usuario

**POST** `/api/users`

```json
{
  "username": "usuario",
  "email": "email@mail.com",
  "password": "password123"
}
```

---

### Login

**POST** `/auth`

```json
{
  "username": "usuario",
  "password": "password123"
}
```

**Respuesta:**

```json
{
  "token": "JWT_TOKEN",
  "refreshToken": "REFRESH_TOKEN"
}
```

---

### Refresh Token

**POST** `/auth/refresh`

```json
{
  "refreshToken": "REFRESH_TOKEN"
}
```

---

## 🔑 Autorización

Incluir en endpoints protegidos:

```http
Authorization: Bearer {{token}}
```

---

## 📀 Albums

### Crear álbum

* ADMIN: `POST /api/albums`
* ARTIST: `POST /api/albums/artist`

```json
{
  "title": "Nombre del álbum",
  "artistName": "Nombre del artista",
  "releaseYear": "2024"
}
```

---

### Listar álbumes

**GET** `/api/albums`

---

### Obtener álbum por ID

**GET** `/api/albums/:id`

---

### Editar álbum

**PATCH** `/api/albums/:id`

---

### Eliminar álbum

**DELETE** `/api/albums/:id`

---

### Aprobar álbum

**PATCH** `/api/albums/:id/status?status=APPROVED`

---

## 🎶 Songs

### Crear canción

* ADMIN: `POST /api/songs`
* ARTIST: `POST /api/songs/artist`

```json
{
  "title": "Nombre de la canción",
  "artistName": "Artista",
  "genre": "Rock",
  "albumId": 1
}
```

---

### Buscar canciones

**GET** `/api/songs`

Query params opcionales:

* `title`
* `genre`
* `artistName`
* `albumId`

Ejemplo:

```
/api/songs?genre=Rock&artistName=Queen
```

---

### Obtener canción por ID

**GET** `/api/songs/:id`

---

### Editar canción

**PATCH** `/api/songs/:id`

---

### Eliminar canción

**DELETE** `/api/songs/:id`

---

### Aprobar canción

**PATCH** `/api/songs/:id/status?status=APPROVED`

---

## 🎸 Song Versions

### Instrument (ENUM)

Cada versión requiere un instrumento:

```
GUITAR | BASS | PIANO
```

Se envía como **query parameter obligatorio**.

---

### Subir versión

* ADMIN:
  `POST /api/songs/:songId/versions?instrument=GUITAR`

* ARTIST:
  `POST /api/songs/:songId/versions/artist?instrument=GUITAR`

**Tipo:** `multipart/form-data`

**Body:**

* `file` → archivo (PDF, Guitar Pro, MusicXML, etc.)

---

### Ejemplo

```
POST /api/songs/1/versions?instrument=GUITAR
```

Form-data:

* file → archivo

---

### Validaciones

* `instrument` obligatorio (GUITAR, BASS, PIANO)
* `file` obligatorio
* `songId` debe existir

---

### Listar versiones

**GET** `/api/songs/:songId/versions`

---

### Obtener versión por ID

**GET** `/api/songs/:songId/versions/:versionId`

---

### Aprobar versión

**PATCH** `/api/songs/:songId/versions/:versionId/status?status=APPROVED`

---

### Eliminar versión

**DELETE** `/api/songs/:songId/versions/:versionId`

---

## 🔐 Roles y permisos

| Acción            | ADMIN | ARTIST |
| ----------------- | ----- | ------ |
| Crear álbum       | ✅     | ✅      |
| Aprobar álbum     | ✅     | ❌      |
| Crear canción     | ✅     | ✅      |
| Aprobar canción   | ✅     | ❌      |
| Subir versiones   | ✅     | ✅      |
| Aprobar versiones | ✅     | ❌      |

---

## 🧪 Flujo recomendado

1. Registrar usuario
2. Login
3. Guardar token
4. Crear álbum
5. Crear canción
6. Subir versión (con instrument)
7. Aprobar contenido (ADMIN)

---

## 📌 Notas

* Los endpoints protegidos requieren JWT válido
* Reemplazar parámetros (`:id`, `:songId`, etc.)
* El sistema usa moderación (aprobación manual)

---

## 📄 Licencia

Uso académico / proyecto personal
