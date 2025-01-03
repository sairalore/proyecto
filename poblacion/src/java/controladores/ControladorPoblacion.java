/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.RequestScoped;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import clases.Provincia;
import clases.Parroquia;
import clases.Canton;

/**
 *
 * @author scaiza
 */
@ManagedBean
@ViewScoped
public class ControladorPoblacion implements Serializable {

    private String v1 = "hOla";

    private JSONObject data;

    private String provinciaSeleccionada;
    private String cantonSeleccionado;
    private String parroquiaSeleccionada;

    private List<Provincia> provincias = new ArrayList<>();
    private List<Canton> cantones = new ArrayList<>();
    private List<Parroquia> parroquias = new ArrayList<>();

    public ControladorPoblacion() {

    }

    @PostConstruct
    public void inicializar() {
        fetchData();
    }

    public void fetchData() {
        try {
            // URL de la API
            String apiUrl = "https://gist.githubusercontent.com/emamut/6626d3dff58598b624a1/raw/166183f4520c4603987c55498df8d2983703c316/provincias.json";
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Leer la respuesta de la API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Convertir la respuesta a JSON
            data = new JSONObject(response.toString());
            for (String provinciaId : data.keySet()) {
                JSONObject provinciaJson = data.getJSONObject(provinciaId);
                String provinciaNombre = provinciaJson.getString("provincia");
                provincias.add(new Provincia(provinciaId, provinciaNombre));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onProvinciaChange1() {
        System.out.println("aqui entro");
    }

    public void onProvinciaChange() {
        // Actualizar cantones según la provincia seleccionada
        cantones.clear();
        JSONObject provincia = data.getJSONObject(provinciaSeleccionada);
        JSONObject cantonesJson = provincia.getJSONObject("cantones");
        System.out.println("cantonesJson: "+cantonesJson);

        for (String cantonId : cantonesJson.keySet()) {
            JSONObject cantonJson = cantonesJson.getJSONObject(cantonId);
            String cantonNombre = cantonJson.getString("canton");
            cantones.add(new Canton(cantonId, cantonNombre));
        }
    }

    public void onCantonChange() {
        // Actualizar parroquias según el cantón seleccionado
        parroquias.clear();
        JSONObject provincia = data.getJSONObject(provinciaSeleccionada);
        JSONObject cantonesJson = provincia.getJSONObject("cantones");
        JSONObject canton = cantonesJson.getJSONObject(cantonSeleccionado);
        JSONObject parroquiasJson = canton.getJSONObject("parroquias");

        for (String parroquiaId : parroquiasJson.keySet()) {
            String parroquiaNombre = parroquiasJson.getString(parroquiaId);
            parroquias.add(new Parroquia(parroquiaId, parroquiaNombre));
        }
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getProvinciaSeleccionada() {
        return provinciaSeleccionada;
    }

    public void setProvinciaSeleccionada(String provinciaSeleccionada) {
        this.provinciaSeleccionada = provinciaSeleccionada;
    }

    public String getCantonSeleccionado() {
        return cantonSeleccionado;
    }

    public void setCantonSeleccionado(String cantonSeleccionado) {
        this.cantonSeleccionado = cantonSeleccionado;
    }

    public String getParroquiaSeleccionada() {
        return parroquiaSeleccionada;
    }

    public void setParroquiaSeleccionada(String parroquiaSeleccionada) {
        this.parroquiaSeleccionada = parroquiaSeleccionada;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Canton> getCantones() {
        return cantones;
    }

    public void setCantones(List<Canton> cantones) {
        this.cantones = cantones;
    }

    public List<Parroquia> getParroquias() {
        return parroquias;
    }

    public void setParroquias(List<Parroquia> parroquias) {
        this.parroquias = parroquias;
    }

}
