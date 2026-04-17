package views;

import data.Persistencia;
import domain.Marca;
import domain.Sucursal;
import domain.Vehiculo;
import domain.VehiculoCombustible;
import domain.VehiculoElectrico;
import domain.VehiculoTipo;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Controlador {
    
    public static ArrayList<VehiculoViewModel> getVehiculos(){
        ArrayList<VehiculoViewModel> vehiculos = new ArrayList<>();
        for(Vehiculo vehiculo : Persistencia.getVehiculos()) {
            vehiculos.add(new VehiculoViewModel(vehiculo));
        }
        return vehiculos;
    }
    
    public static void agregarVehiculosElec(String patente, String marca, String modelo, int anio, double capacidadCarga, String sucursal, double kwhBase){
        Persistencia.agregarVehiculo(new VehiculoElectrico(patente,getMarca(marca),modelo,anio,capacidadCarga,getSucursal(sucursal),kwhBase));
    }
    
    public static void agregarVehiculosComb(String patente, String marca, String modelo, int anio, double capacidadCarga, String sucursal, double kilometrosPorLitro, double litrosExtra){
        Persistencia.agregarVehiculo(new VehiculoCombustible(patente,getMarca(marca),modelo,anio,capacidadCarga,getSucursal(sucursal),kilometrosPorLitro,litrosExtra));
    }
    
    public static ArrayList<Sucursal> getSucursalesDisponibles(){
        return Persistencia.getSucursales();
    }
    
    public static ArrayList<Marca> getMarcasDisponibles(){
        return Persistencia.getMarcas();
    }
    
    public static Marca getMarca(String nombre){
        for(Marca marca : getMarcasDisponibles()){
            if(marca.getNombre().equals(nombre)){
                return marca;
            }
        }
        return null;
    }
    
    public static Sucursal getSucursal(String nombre){
        for(Sucursal sucursal : getSucursalesDisponibles()){
            if(sucursal.getCodigo().equals(nombre)){
                return sucursal;
            }
        }
        return null;
    }
    
    public static double[] calcularConsumos(Map<String, Double> vehiculos){
        double consumoElectricos = 0;
        double consumoCombustible= 0;
        for(Map.Entry<String, Double> entry : vehiculos.entrySet()){
           double consumo = 0;
           Optional<Vehiculo> vehiculo = Persistencia.getVehiculo(entry.getKey());
           if(vehiculo.isPresent()){
               consumo = vehiculo.get().calcularConsumo(entry.getValue());
               consumoElectricos += vehiculo.get().esDe(VehiculoTipo.ELECTRICO) ? consumo : 0;
               consumoCombustible += vehiculo.get().esDe(VehiculoTipo.COMBUSTIBLE) ? consumo : 0;
           }
        }
        return new double[] {consumoElectricos, consumoCombustible};
    }
}
