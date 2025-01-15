# clinicAssetmentSpringoot

# Casos de Uso y Lógica de Negocio - Sistema de Gestión de Citas Médicas

## **Paciente**

### 1. **Agendar una cita médica**
- Como paciente quiero poder agendar una cita médica.

### 2. **Consultar citas asignadas**
- Como paciente quiero poder ver todas las citas que tengo asignadas y la información detallada de las mismas (estado de la cita, hora, síntomas, historial de citas).

### 3. **Reprogramar una cita**
- Como paciente quiero poder reprogramar una cita, cambiando el horario o la fecha de la cita según la disponibilidad del médico.

## **Médico**

### 1. **Ver citas agendadas**
- Como médico quiero poder ver todas las citas que tengo agendadas en el día.

### 2. **Reprogramar una cita**
- Como médico quiero poder reprogramar una cita, cambiando el horario o la fecha según la disponibilidad.

### 3. **Reagendar una cita**
- Como médico quiero poder reagendar una cita previamente cancelada o no atendida.

## **Administrador**

### 1. **Gestionar usuarios**
- Como administrador quiero poder:
    - Crear usuarios y asignarles un rol (paciente, médico, administrador).
    - Eliminar usuarios.
    - Actualizar la información de un usuario.
    - Ver la lista de usuarios registrados y su información detallada.

### 2. **Gestionar citas**
- Como administrador quiero poder ver las citas programadas a los médicos.
- Asignar el horario de trabajo de los médicos.
- Asignar la duración de las citas médicas.

### 3. **Acceso total**
- Como administrador quiero tener acceso a todas las funcionalidades de la aplicación.

---

## **Lógica de Negocio**

### **Agendar una cita médica**

#### **Requisitos del paciente**
1. El paciente debe estar registrado en el sistema.
2. El paciente debe tener autorización para acceder al endpoint de agendamiento, es decir, tener el rol de "paciente".
3. El paciente debe suministrar la siguiente información:
    - Fecha de la cita.
    - Hora de la cita.
    - Escoger un medio de los disponibles.
    - Motivo de la cita.

#### **Validaciones del sistema**
1. La cita debe tener un médico asignado.
2. La cita debe incluir:
    - Fecha y hora.
    - Motivo de la cita.
    - ID del paciente que solicita la cita.
    - Estado inicial: **"Pendiente"**.
3. El sistema debe verificar:
    - La disponibilidad del médico en la fecha y hora solicitada (validar que esté dentro de su horario de trabajo).
    - Que la hora suministrada por el paciente no esté ya ocupada por otra cita con estado **"Pendiente"**.
4. Se debe tener en cuenta la duración de la cita para permitir el agendamiento en bloques de tiempo adecuados.

---

### **Reprogramar una cita**

1. El sistema debe cambiar el estado de la cita anterior a **"Disponible"**.
2. El paciente debe seleccionar un nuevo bloque de horario disponible.
3. La nueva cita debe cumplir con las mismas validaciones de disponibilidad que en el agendamiento inicial.

---

### **Cancelar una cita**

1. La cita debe cambiar su estado de **"Pendiente"** a **"Disponible"**.
2. Alternativamente, el sistema puede eliminar el registro de la cita, permitiendo que ese horario quede disponible para otros usuarios.

---

### **Reagendar una cita**

1. El sistema debe permitir al médico o paciente seleccionar un nuevo bloque de horario.
2. Se deben realizar las mismas validaciones de disponibilidad y duración que en el agendamiento inicial.

## **Generación Dinámica de Horarios Disponibles**

El sistema genera dinámicamente los bloques de tiempo disponibles para los médicos según su horario base y la duración de cada cita. A continuación se describe el proceso:

### 1. **Consultar el horario base del médico**
Cada médico tiene un horario de trabajo fijo que está almacenado en la base de datos. Ejemplo:
- **Horario inicio:** 8:00 a.m.
- **Horario fin:** 4:00 p.m.
- **Duración de cada cita:** 30 minutos.

### 2. **Dividir el horario en bloques**
A partir del horario base y la duración de las citas, el sistema divide el tiempo en bloques consecutivos.
Ejemplo, si la duración de la cita es de 30 minutos, los bloques generados serían:
- **8:00 a 8:30**
- **8:30 a 9:00**
- **9:00 a 9:30**, y así sucesivamente hasta completar el horario del día.

### 3. **Verificar disponibilidad de cada bloque**
El sistema consulta las citas ya agendadas en la base de datos y revisa qué bloques de tiempo ya están ocupados.
- Si un bloque ya está ocupado por una cita **Pendiente** o **Confirmada**, el sistema lo descarta.
- Si un bloque está libre, el sistema lo añade a la lista de horarios disponibles.

### 4. **Devolver la lista de bloques disponibles**
Una vez verificada la disponibilidad, el sistema devuelve al paciente la lista de horarios disponibles para que pueda elegir uno.

---

## **Consideraciones importantes**

###  **Validación de concurrencia**
Si varios pacientes intentan agendar una cita al mismo tiempo, el sistema debe revisar nuevamente la disponibilidad antes de confirmar la cita, para evitar que dos personas reserven el mismo bloque. Esto se puede lograr utilizando mecanismos de bloqueo a nivel de base de datos o transacciones atómicas para garantizar que solo un paciente pueda tomar el bloque de tiempo solicitado.

---

## **Concorrencia en el Sistema de Gestión de Citas Médicas**

### **Descripción del problema**
En un sistema en el que varios usuarios pueden intentar agendar una cita en el mismo horario, existe el riesgo de que dos pacientes reserven el mismo bloque de tiempo simultáneamente. Esto puede causar inconsistencias y errores, como la doble reserva de una misma franja horaria.

### **Solución propuesta**
Para evitar que dos pacientes agenden la misma cita en el mismo bloque de tiempo, se deben implementar mecanismos de **concurrencia** que gestionen el acceso a los horarios disponibles de forma controlada.

### **Conclusión**

El sistema debe asegurarse de que dos pacientes no reserven el mismo bloque de tiempo simultáneamente mediante un manejo adecuado de la concurrencia, utilizando mecanismos como bloqueos a nivel de base de datos, transacciones atómicas, y validaciones de disponibilidad antes de confirmar la cita. Esto evitará errores y garantizará una experiencia confiable para los usuarios.

