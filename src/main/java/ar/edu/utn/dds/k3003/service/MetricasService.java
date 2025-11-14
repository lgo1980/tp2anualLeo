package ar.edu.utn.dds.k3003.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class MetricasService {

    private final Counter busquedasCounter;
    private final Timer busquedasTimer;

    public MetricasService(MeterRegistry meterRegistry) {
        this.busquedasCounter = Counter.builder("busquedas.total")
                .description("Número total de búsquedas realizadas")
                .register(meterRegistry);

        this.busquedasTimer = Timer.builder("busquedas.tiempo")
                .description("Tiempo de duración de las búsquedas en milisegundos")
                .register(meterRegistry);
    }

    public void registrarBusqueda() {
        busquedasCounter.increment();
    }

    public Timer getBusquedasTimer() {
        return busquedasTimer;
    }

    public void ejecutarConMetricas(Runnable busqueda) {
        busquedasCounter.increment();
        busquedasTimer.record(busqueda);
    }
}
