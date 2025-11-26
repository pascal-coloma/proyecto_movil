Medbusq

Pascal Coloma
Cristóbal Zeppelin

Nuestra aplicación esta dedicada para la búsqueda de medicamentos utilizando una API de Fonasa y asi logramos buscar los medicamentos según la ciudad.

Endpoints:
Propios;
http://54.161.22.247:8080/
GET
/medbusq/v1/usuario
/medbusq/v1/usuario/{rut}
/medbusq/v1/ciudad
/medbusq/v1/ciudad/{id}
POST
/medbusq/v1/usuario
PUT
/medbusq/v1/usuario/{rut}

Externos:
http://54.161.22.247:3000/
GET
/api/medicamento
