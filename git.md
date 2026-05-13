# 🧭 CentroPlus Connect — Guía de trabajo con Git

Guía para el equipo sobre cómo trabajar con ramas, commits, push y merges usando Git y GitHub.

---

## 🧠 Flujo de trabajo

En Git seguimos este flujo:

feature → commit → push → pull request → develop → main

---

## 🌿 1. Estructura de ramas

### 🔴 main
- Versión final del proyecto
- Solo código estable

### 🟡 develop
- Rama de integración del equipo
- Aquí se unen todas las features

### 🟢 feature/*
- Trabajo por funcionalidades

Ejemplos:
- feature/backend
- feature/mobile
- feature/web
- feature/database
- feature/docker
- feature/docs

---

## 🚀 2. Empezar a trabajar

### 🔄 Actualizar develop

git checkout develop  
git pull  

---

### 🌱 Crear una feature

git checkout -b feature/backend  
git push -u origin feature/backend  

---

## 💻 3. Trabajar en el código

- Crear archivos  
- Modificar código  
- Implementar funcionalidades  

---

## 💾 4. Guardar cambios (commit)

git add .  
git commit -m "feat: descripción del cambio"  

---

### 🧠 Tipos de commit

- feat: nueva funcionalidad  
- fix: corrección de errores  
- docs: documentación  
- test: pruebas  
- refactor: mejoras internas  

---

### 🧪 Ejemplo

git commit -m "feat: crea login de usuario"  

---

## 📤 5. Subir cambios (push)

git push  

Esto sube los cambios a GitHub.

---

## 🔀 6. Pull Request (GitHub)

Cuando termines una feature:

feature/backend → develop  

---

## 👀 7. Revisión

Se revisa:

- funcionamiento  
- calidad del código  
- estructura  
- errores  

---

## ✅ 8. Merge

Si todo está correcto:

- Se acepta el Pull Request  
- El código pasa a develop  

---

## 🔁 9. Volver a trabajar

git checkout develop  
git pull  

---

### 🌱 Crear nueva feature

git checkout -b feature/nueva-funcionalidad  

---

## 🏁 10. Final del proyecto

develop → main  

---

## ⚠️ Reglas importantes

- ❌ No trabajar en main  
- ❌ No mezclar features  
- ❌ Commits pequeños y claros  
- ❌ Siempre hacer push  
- ✅ Siempre trabajar en feature/*