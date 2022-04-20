/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Alcancia;
import Model.Moneda;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author dell
 */
public class MonederoController {
    private static ArrayList<Alcancia> Alcancia = new ArrayList<Alcancia>();
    
    
    //Añade una moneda
    public void Adicionar(Moneda m,int Cantidad){
        this.Alcancia.add(new Alcancia(Cantidad,m));
    }
    
    
    
    //Añade un array de moneda
    public void Adicionar(ArrayList<Moneda> m){
        for(int i=0;i<m.size();i++){
            int id = this.IndexDeAlcancia(m.get(i).getPrecio());
            Adicionar(m.get(i),1);
        }
    }
  
    
    
    //extrae el dinero y lo devuelve en monedas
    public boolean Sacar(int dinero){
        boolean error = false;
        ArrayList<Alcancia> valores = EntregarMonedasPorMonto(dinero);
        ArrayList<Alcancia> MonedasDescontadas = new ArrayList<Alcancia>();
        
        //recorremos los valores de los dineros entregados
        for(int i=0;i<valores.size();i++){
            int index = IndexDeAlcancia(valores.get(i).getM().getPrecio());
        
            //preguntamos si la cantidad del monedas existe
            boolean existeMonto = VerificarCantidadMonedasEnAlcancia(valores.get(i),Alcancia);

            if(!existeMonto) {
                //consultamos los equivaletes a esa moneda
                ArrayList<Alcancia> equivalencia =  EquivalenciaDeMonedas(valores.get(i),Alcancia);
                
                //recorremos los equivalentes para ir restando uno a uno
                for (int j = 0; j < equivalencia.size(); j++) {
                    int descontar = equivalencia.get(j).getnMonedas();
                    Moneda m =  equivalencia.get(j).getM();
                    Alcancia ap = equivalencia.get(j);
                    MonedasDescontadas.add(new Alcancia(descontar,equivalencia.get(index).getM()));
                    ap.setnMonedas(ap.getnMonedas()-descontar);
                    restarMonedasByIndex(IndexDeAlcancia(m.getPrecio()), descontar);
                }
            }else{
                int descontar = valores.get(i).getnMonedas();
                Moneda m = valores.get(i).getM();
                restarMonedasByIndex(index,descontar);
                MonedasDescontadas.add(new Alcancia(descontar,m));
            }
           
        }
        for(int d=0;d<MonedasDescontadas.size();d++){
                System.out.println(MonedasDescontadas.get(d).getM().getPrecio()+"|"+MonedasDescontadas.get(d).getnMonedas() +" Monedas Descontadas");
            }
        //ya te devuelve la cantidad exacta que proporcionas, solo falta contar los billetes si los tiene todos
        //y si no , implementar una funcion que reemplace un billete por otros de menos valor, y asi hasta que diga que no tiene mas dinero
        return false;
    }
    
    
    
    //la funcion devuelve una alcancia con las monedas dependiendo del monto que se solicite
    
    public ArrayList<Alcancia> EntregarMonedasPorMonto(int dinero ){
        ArrayList<Alcancia> prueba = new ArrayList<Alcancia>();
        while(dinero>0){
            switch((dinero >=1000)?1000:
                    (dinero >=500)?500:
                    (dinero >=200)?200:
                    (dinero >=100)?100:50 ){
                case 1000:
                    prueba = AgruparMonedaPorAlcancia(new Moneda(1000), 1 , prueba);
                    dinero-=1000;
                    break;
                case 500:
                    prueba = AgruparMonedaPorAlcancia(new Moneda(500), 1 , prueba);
                    dinero-=500;
                    break;
                case 200:
                    prueba = AgruparMonedaPorAlcancia(new Moneda(200), 1 , prueba);
                    dinero-=200;
                    break;
                case 100:
                    prueba =AgruparMonedaPorAlcancia(new Moneda(100), 1 , prueba);
                    dinero-=100;
                    break;
                case 50:
                    prueba = AgruparMonedaPorAlcancia(new Moneda(50), 1 , prueba);
                    dinero-=50;
                    break;
            }
        }
        /*for (int i = 0; i < prueba.size(); i++) {
            System.out.println(prueba.get(i).getM().getPrecio()+"|"+prueba.get(i).getnMonedas() +"Montos de Entregar Monedas Por Monto");   
        }*/
        return prueba;
    }

    
    
    private ArrayList<Alcancia> AgruparMonedaPorAlcancia(Moneda m, int c, ArrayList<Alcancia> a){
        if(VerificarExisteMonedaAlcancia(m,a)){
            int i = IndexDeAlcancia(m.getPrecio(),a);
            if(i!=-1){
                a.get(i).setnMonedas(a.get(i).getnMonedas()+c);
            }else{
                a.add(new Alcancia(c,m));
            }
        }else{
            a.add(new Alcancia(c,m));
        }
        return a;
    }
    
    
    
    //cuenta el numero de monedas que esta en la alcancia | este se complementa con MontoMonedasPequeñaDenominacion
    private boolean VerificarCantidadMonedasEnAlcancia(Alcancia AlcanciaConMonedas, ArrayList<Alcancia> arr){
        boolean Posible = true;
        int i = IndexDeAlcancia(AlcanciaConMonedas.getM().getPrecio(),arr);
        if(i==-1 || arr.get(i).getnMonedas() < AlcanciaConMonedas.getnMonedas()){
            return Posible = false;
        } 
        return Posible;
    }
    
   

    //esta funcion se ejecuta cuando las monedas se acaban y toca buscar una alternativa a las monedas:
    //ejem: 1000 = 500 + 500 || 1000 = 200 + 500 + 100 +200
    private ArrayList<Alcancia> EquivalenciaDeMonedas(Alcancia a,ArrayList<Alcancia> arr){
        ArrayList<Alcancia> at = new ArrayList<Alcancia>();
        for (int i = 0; i < a.getnMonedas(); i++) {
            if(a.getM().getPrecio()==1000){
                if(VerificarCantidadMonedasEnAlcancia(new Alcancia(2,new Moneda(500)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(500),2, at);
                }else if(VerificarCantidadMonedasEnAlcancia(new Alcancia(5,new Moneda(200)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(200),5, at);
                }else if(VerificarCantidadMonedasEnAlcancia(new Alcancia(10,new Moneda(100)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(100),10, at);
                }else if(VerificarCantidadMonedasEnAlcancia(new Alcancia(20,new Moneda(50)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(50),20, at);
                }
            }else if(a.getM().getPrecio()==500){
                if(VerificarCantidadMonedasEnAlcancia(new Alcancia(2,new Moneda(200)),arr) && (VerificarCantidadMonedasEnAlcancia(new Alcancia(1,new Moneda(100)), arr))){
                    at = AgruparMonedaPorAlcancia(new Moneda(200),2, at);
                    at = AgruparMonedaPorAlcancia(new Moneda(100),1, at);
                }else if(VerificarCantidadMonedasEnAlcancia(new Alcancia(5,new Moneda(100)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(100),5, at);
                }else if(VerificarCantidadMonedasEnAlcancia(new Alcancia(10,new Moneda(50)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(50),10, at);
                }
            }
            else if(a.getM().getPrecio()==200){
                if(VerificarCantidadMonedasEnAlcancia(new Alcancia(2,new Moneda(100)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(100),2, at);
                }else if(VerificarCantidadMonedasEnAlcancia(new Alcancia(4,new Moneda(50)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(50),4, at);
                }
            }
            else if(a.getM().getPrecio()==100){
                if(VerificarCantidadMonedasEnAlcancia(new Alcancia(2,new Moneda(50)), arr)){
                    at = AgruparMonedaPorAlcancia(new Moneda(50),2, at);
                }
            }  
        }
        
        return at;
    }
    
    
    
    
    //resta una moneda a la alcancia global
    private boolean restarMonedasByIndex(int index,int cantidadDescontadas){
        boolean result=false;
        int numeroMonedas = this.Alcancia.get(index).getnMonedas();
        if(numeroMonedas>0){
            this.Alcancia.get(index).setnMonedas(numeroMonedas-cantidadDescontadas);
            result=true;
        }
        
        return result;
    }
    
    
    
    //comprueba la existencia de la moneda en una alcancia
    private boolean VerificarExisteMonedaAlcancia(Moneda m, ArrayList<Alcancia> a){
        boolean exist=false;
        for(int i=0;i<a.size();i++){
            if(a.get(i).getM().getPrecio()==m.getPrecio()){
                exist = true;
                break;
            }
        }
        return exist;
    }
   
    
    
    //devuelve el indice de la alcancia global
    private int IndexDeAlcancia(int m){
        int nm =0;
        for(int i=0;i<Alcancia.size();i++){
            if(Alcancia.get(i).getM().getPrecio()== m){
               nm=i;
            }
        }
        return nm;
    }
    
    
    
    //devuelve el indice de la alcancia seteada 
    private int IndexDeAlcancia(int m,ArrayList<Alcancia> a){
        int nm =-1;
        for(int i=0;i<a.size();i++){
            if(a.get(i).getM().getPrecio()== m){
                System.out.println(a.get(i).getM().getPrecio()+"|"+a.get(i).getnMonedas()+" padre");
                nm=i;
            }
        }
        return nm;
    }
    
    
    
     //cantidad de monedas por denominacion
    public int CantidadPorMoneda(Moneda m){
        int nm =0;
        for(int i=0;i<Alcancia.size();i++){
            if(Alcancia.get(i).getM().getPrecio()==m.getPrecio()){
               nm = Alcancia.get(i).getnMonedas();
               break;
            }
        }
        return nm;
    }
    
   
    
    //cantidad de dinero en total
    public int CantidadDinero(){
        int cant=0;
        for(int i=0;i<Alcancia.size();i++){
            int cantMon = Alcancia.get(i).getnMonedas();
            int precio = Alcancia.get(i).getM().getPrecio();
            cant += precio*cantMon;
        }
        return cant;
    }
    
    
   
     //cantidad de monedas
    public int CantidadDeMonedas(){
        int cant=0;
        for(int i=0;i<Alcancia.size();i++){
            cant += Alcancia.get(i).getnMonedas();
        }
        return cant;
    }
    
}
