package com.mellenamongush.challengeLiterAlura.servicios;

public interface IConvierteDatosInt {
    <T> T obtenerDatos (String json, Class<T> clase);
}
