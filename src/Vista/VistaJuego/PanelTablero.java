package Vista.VistaJuego;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.IJugador;
import Modelo.Posicion;
import Modelo.Tablero;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelTablero extends JPanel {

    private ControladorJuego controlador;
    private PanelJugador panelJugador;
    private Tablero tablero;
    private final int ANCHO_CASILLERO = 65;
    private final int ALTO_CASILLERO = 100;
    private IJugador jugadorCliente;

    private Map<String, BufferedImage> cacheImagenes = new HashMap<>();

    private int lastMouseX;
    private int lastMouseY;

    // Offset para desplazamiento (en píxeles de pantalla)
    private double offsetX = 0;
    private double offsetY = 0;

    // Zoom (escala)
    private double zoom = 1.0;
    private final double MIN_ZOOM = 0.3;
    private final double MAX_ZOOM = 3.0;

    // Flag para saber si es la primera vez que se dibuja
    private boolean primerDibujado = true;

    public PanelTablero(PanelJugador jugador, ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.panelJugador = jugador;
        this.jugadorCliente = controlador.getJugadorActualizado();
        this.tablero = controlador.getTablero();


        setBackground(Color.decode("#4b3e2c"));
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        // Mouse listener para arrastrar
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    manejarClick(e.getX(), e.getY());
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Actualizar el offset basado en el desplazamiento del mouse
                offsetX += e.getX() - lastMouseX;
                offsetY += e.getY() - lastMouseY;
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                repaint();
            }
        });

        // Mouse wheel listener para zoom
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Guardar zoom anterior
                double oldZoom = zoom;

                // Calcular nuevo zoom
                if (e.getWheelRotation() < 0) {
                    zoom *= 1.1; // Zoom in
                } else {
                    zoom /= 1.1; // Zoom out
                }

                // Limitar zoom
                zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom));

                // Ajustar offset para que el zoom sea hacia el cursor
                Point mousePos = e.getPoint();
                offsetX = mousePos.x - (mousePos.x - offsetX) * (zoom / oldZoom);
                offsetY = mousePos.y - (mousePos.y - offsetY) * (zoom / oldZoom);

                repaint();
            }
        });

        repaint();
    }

    private BufferedImage obtenerImagen(String ruta, Class<?> claseRef) {
        if (!cacheImagenes.containsKey(ruta)) {
            try {
                URL url = claseRef.getResource("/" + ruta);
                if (url != null) {
                    BufferedImage img = ImageIO.read(url);
                    cacheImagenes.put(ruta, img);
                }
            } catch (Exception e) {
                System.err.println("Error cargando: " + ruta);
                e.printStackTrace();
            }
        }
        return cacheImagenes.get(ruta);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Activar antialiasing para mejor calidad visual
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Solo centrar el tablero la primera vez
        if (primerDibujado && !tablero.getCartas().isEmpty()) {
            centrarTablero();
            primerDibujado = false;
        }

        // Dibujar una grilla de fondo
        dibujarGrilla(g2);

        // dibujar la cartas
        for (var entry : tablero.getCartas().entrySet()) {
            Posicion p = entry.getKey();
            Carta carta = entry.getValue();

            int xPantalla = (int) (p.getY() * ANCHO_CASILLERO * zoom + offsetX);
            int yPantalla = (int) (p.getX() * ALTO_CASILLERO * zoom + offsetY);

            try {
                BufferedImage imgOriginal = null;
                try {
                    if (carta != null) {
                        imgOriginal = obtenerImagen(carta.getImg(), carta.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (imgOriginal != null) {
                    int anchoEscalado = (int) (ANCHO_CASILLERO * zoom);
                    int altoEscalado = (int) (ALTO_CASILLERO * zoom);

                    // Crear imagen escalada
                    BufferedImage imgEscalada = new BufferedImage(
                            anchoEscalado, altoEscalado, BufferedImage.TYPE_INT_ARGB
                    );

                    Graphics2D g2d = imgEscalada.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(imgOriginal, 0, 0, anchoEscalado, altoEscalado, null);
                    g2d.dispose();

                    g2.drawImage(imgEscalada, xPantalla, yPantalla, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dibujarGrilla(Graphics2D g2) {
        g2.setColor(new Color(100, 80, 60, 50)); // Color semi-transparente

        int spacing = (int) (ANCHO_CASILLERO * zoom);

        // Líneas verticales
        for (int x = (int) offsetX % spacing; x < getWidth(); x += spacing) {
            g2.drawLine(x, 0, x, getHeight());
        }

        // Líneas horizontales
        spacing = (int) (ALTO_CASILLERO * zoom);
        for (int y = (int) offsetY % spacing; y < getHeight(); y += spacing) {
            g2.drawLine(0, y, getWidth(), y);
        }
    }

    private void centrarTablero() {
        if (tablero.getCartas().isEmpty()) return;

        // Calcular bounds en coordenadas del MUNDO (no píxeles)
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Posicion p : tablero.getCartas().keySet()) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }

        // Calcular el centro del tablero en coordenadas del mundo
        double centroMundoX = (minX + maxX) / 2.0;
        double centroMundoY = (minY + maxY) / 2.0;

        // Convertir a píxeles y centrar en la pantalla
        offsetX = getWidth() / 2.0 - centroMundoY * ANCHO_CASILLERO * zoom;
        offsetY = getHeight() / 2.0 - centroMundoX * ALTO_CASILLERO * zoom;
    }

    private void manejarClick(int mouseX, int mouseY) throws RemoteException {
        // Convertir de coordenadas de pantalla a coordenadas del mundo
        int y = (int) Math.floor((mouseX - offsetX) / (ANCHO_CASILLERO * zoom));
        int x = (int) Math.floor((mouseY - offsetY) / (ALTO_CASILLERO * zoom));

        System.out.println("Click en pantalla: (" + mouseX + ", " + mouseY + ")");
        System.out.println("Click en mundo: (" + x + ", " + y + ")");

        jugarEnPosicion(x, y);
    }

    private void jugarEnPosicion(int x, int y) throws RemoteException {
        int cartaSeleccionada = panelJugador.getCartaSeleccionada();
        boolean pudoSerJugado = false;

        if (cartaSeleccionada != -1) {
            Carta carta = jugadorCliente.elegirCarta(cartaSeleccionada);

            pudoSerJugado = controlador.jugarUnaCarta(x, y, cartaSeleccionada, null);

            if (!pudoSerJugado) {
                List<Herramienta> tieneHerramientasRotas = jugadorCliente.getHerramientasRotas();

                if (tablero.getCarta(x, y) instanceof CartaTunel) {
                    JOptionPane.showMessageDialog(this, "No se puede jugar ahí");
                } else if (!tieneHerramientasRotas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se puede construir con herramientas rotas");
                }
            } else {
                // Manejar cartas especiales
                Carta cartaJugada = tablero.getCarta(x, y);
                if (carta instanceof CartaAccion) {
                    CartaAccion accion = (CartaAccion) carta;
                    if (accion.getTipoAccion().getFirst() == TipoAccion.MAPA) {
                        if (cartaJugada instanceof CartaDestino) {
                            CartaDestino destino = (CartaDestino) cartaJugada;
                            ImageIcon icono = new ImageIcon(
                                    destino.getClass().getResource("/" + destino.getCara())
                            );
                            icono = new ImageIcon(icono.getImage().getScaledInstance(
                                    ANCHO_CASILLERO, ALTO_CASILLERO, Image.SCALE_SMOOTH
                            ));
                            JOptionPane.showMessageDialog(this, new JLabel(icono));
                        }
                    }
                }
            }
        }

        panelJugador.resetCartaSeleccionada();

        if (pudoSerJugado) {
            boolean terminoLaRonda = controlador.verificarSiTerminoLaRonda();
            if(!terminoLaRonda){
                controlador.pasarTurno();
            }
        }

        repaint();
    }

    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(getRootPane(), "No es tu turno!");
    }

    public void actualizar(Tablero tablero, IJugador jugadorCliente) {
        this.jugadorCliente = jugadorCliente;
        this.tablero = tablero;
        revalidate();
        repaint();
    }
}