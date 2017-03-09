package mont.gonzalo.phiuba.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gonzalo Montiel on 1/8/17.
 */

public class News implements Serializable{
    private String title;
    private String created;
    private String thumbnail;
    private String text;
    private String img;
    private String link;

    public  News(String link, String title, String thumbnail, String text, String img) {
        this.link = link;
        this.title = title;
        this.thumbnail = thumbnail;
        this.text = text;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    static private List<News> sample_data;

    public static List<News> getSampleData() {
        if (sample_data == null) {
            List<News> nn = new ArrayList<>();
            nn.add(new News("http://www.fi.uba.ar/es/node/2096", "Plataforma renovada", "http://www.fi.uba.ar/sites/default/files/styles/miniatura_noticia/public/field/image/Campus%203-1%20web.jpg?itok=eHmL5EYv", "El Centro de Educación a Distancia informa que ya está on line una nueva versión del campus institucional de la FIUBA, que incorpora mejoras a funcionalidades básicas y también novedades que permiten una interacción mucho más dinámica con esta plataforma de enseñanza.  \nEntre las mejoras que permite esta nueva versión, se destacan:\n_ Usabilidad mejorada para estudiantes con un tablero que concentra\nmensajes, tareas pendientes, calificaciones.\n_ Mayor fluidez y practicidad en envío y corrección de tareas, con la función de agregar anotaciones en las devoluciones. \n_ Mejoras en el etiquetado y edición de cursos y en actividades, tales como foros, talleres\no lecciones.\n_ Descarga masiva de archivos y materiales.\n_ Mejoras en el envío de mensajes internos.\n_ Nuevos tipos de ejercicios para elaborar cuestionarios y encuestas.\n_ Carpeta personal de archivos.\nEntre las nuevas aplicaciones:\n_ Entorno responsivo que puede verse desde dispositivos móviles.\n_ Calificación basada en competencias.\n_ Calificación con insignias.\n_ Mayor facilidad en la recuperación de archivos con la nueva papelera de reciclaje.\n_ Hilos fijos en los foros y enlaces permanentes.\n_ Búsquedas globales haciendo más sencillo acceder a los contenidos de toda la plataforma Moodle.\n_ Bookmarks de sitios relevantes en el campus.\n_ Repositorio de archivos externos.\nVer Campus FIUBA, versión 3.1\nConsultas:\nCentro de Educación a Distancia\nAv. Paseo Colón 850, 2do. Piso, C1063ACV, Buenos Aires, Argentina.\n4343-0893/0092 int. 1110cead@fi.uba.ar", "http://www.fi.uba.ar/sites/default/files/field/image/Campus%203-1%20web.jpg" ));
            nn.add(new News("http://www.fi.uba.ar/es/node/2126", "Preinscripción", "http://www.fi.uba.ar/sites/default/files/styles/miniatura_noticia/public/field/image/IngenieriaFerroviaria-01_0.jpg?itok=G82cXRJ7","Hasta el 30 de noviembre de 2016 estará abierta la preinscripción al VII curso de la Carrera de Especialización en Ingeniería Ferroviaria que dicta la Escuela de Ing. Ferroviaria de la FIUBA. \nEsta oferta de posgrado, destinada a ingenieros graduados y a profesionales egresados de carreras afines con formación básica físico-matemática equivalente a la de ingeniería, tiene como objetivo la profundización y actualización de conocimientos en materias de naturaleza técnica, económica e institucional, que hacen al funcionamiento de los ferrocarriles, así como la complementación de dichos conceptos con aspectos vinculados a la economía y las finanzas. \nCondiciones de ingreso: \n. Graduados FIUBA con título de ingeniero o carreras afines de cuatro años de duración mínima, que incluyan materias con conocimientos físico-matemáticos.\n. Graduados de otras universidades argentinas o extranjeras, con títulos equivalentes.\nInicio 1er. cuatrimestre: 17 de marzo de 2017.\nDuración: cuatro cuatrimestres, 554 horas lectivas.\nHorarios de clase: viernes de 14.00 a 20.30 y sábados de 10.00 a 17.00, en la sede de Av. Las Heras 2214, C.A.B.A.\n:: Plan de Estudios\nMódulo 1: materias que tratan la infraestructura ferroviaria, cuyos contenidos corresponden principalmente a la ingeniería civil.\nMódulo 2: materias relativas a la operación y equipamiento, con contenidos afines a las ingenierías mecánica, electromecánica, electricista, electrónica e industrial.\nMódulo 3: materias con contenidos económicos, institucionales, históricos, legales, etc.\nPerfil del egresado: profesional con una visión integral de la técnica ferroviaria.\nBecas disponibles para egresados de la FIUBA que no provengan de empresas.\nVer más sobre la carrera de especialización\nContacto:\nAv. Las Heras 2214 - 2do. piso - C1127AAR - Buenos Aires - Argentina\nTel.: (54-11) 4514-3020 Int. 112 \nHorario de atención: lunes a viernes de 17.00 a 20.00.\nE-mail: eferroviaria@fi.uba.ar", "http://www.fi.uba.ar/sites/default/files/field/image/IngenieriaFerroviaria-01_0.jpg"));
            nn.add(new News("http://www.fi.uba.ar/es/node/2218", "Inscripción abierta", "http://www.fi.uba.ar/sites/default/files/styles/miniatura_noticia/public/field/image/Industrial_0_0.jpg?itok=U55F45iL","La Escuela de Graduados Ingeniería de Dirección Empresaria (EGIDE) de la FIUBA ha abierto la inscripción para el Curso Bianual de la Maestría en Dirección Industrial cuya 13ra. promoción comenzará en abril del 2017.\nEsta maestría está dirigida a todos aquellos profesionales en ejercicio de tareas propias de la conducción empresarial a fin de capacitarlos en la organización y dirección de empresas industriales y de servicios.  Está encuadrada dentro de la seriedad académica de la Universidad de Buenos Aires y otorga a los graduados el Título de Magíster de la Universidad de Buenos Aires.\n:: Reunión informativa\nLunes 7 de noviembre, desde las 18.00, en el Aula 308 de la sede de Av. Paseo Colón 850.\n \nPara mayor información, dirigirse al (011) 4331-4987/0172, en el horario de 16.00 a 20.00, ó vía mail a egide@fi.uba.ar", "http://www.fi.uba.ar/sites/default/files/field/image/Industrial_0_0.jpg"));
            nn.add(new News("http://www.fi.uba.ar/es/node/2244", "Nuevos profesionales", "http://www.fi.uba.ar/sites/default/files/styles/miniatura_noticia/public/field/image/Jura%202016-10-27.jpg?itok=TDzK1PNZ","El pasado 27 de octubre, en las instalaciones del Salón Ing. Humberto Ciancaglini de la sede de Av. Paseo Colón 850 de la FIUBA, se llevó a cabo un nuevo acto de colación, en el que participaron autoridades, docentes, personal no-docente y familiares de los alumnos graduados.\nEl evento contó con las palabras del Ing. Fernando Horman, secretario de Relaciones Institucionales de esta Casa de Estudios, quien hizo mención al rol que los ingenieros deberán asumir como resultado del avance tecnológico en las sociedades modernas. \"El mundo evoluciona hacia una forma diferente de empleo. Un mundo donde se trabaja a distancia, con oficinas abiertas y servicios de un país a otro. En este contexto, la tecnología, que es lo que ustedes manejan, pronto será parte de todas las profesiones del mundo. Ni se imaginan la importancia que tendrán como ingenieros en los próximos años. Los ingenieros serán los grandes generadores de empleo del futuro y es por eso que tienen la obligación de asegurar que la calidad de vida en el planeta esté cada día mejor\", dijo. \nPor su parte, el Ing. Javier Martínez Álvarez, director General de Tenaris Argentina, felicitó a los nuevos profesionales por graduarse en una carrera que \"les da una estructura, un orden y una manera profesional de hacer las cosas que impacta en la realidad de la sociedad y los ubica en un grupo absolutamente privilegiado del país: ingenieros y licenciados graduados en la Universidad de Buenos Aires, la escuela pública de excelencia. Es por eso que tienen la obligación de contribuir, desde su lugar, a una Argentina mejor\".", "http://www.fi.uba.ar/sites/default/files/field/image/Jura%202016-10-27.jpg"));
            nn.add(new News("http://www.fi.uba.ar/es/node/2247", "En sede Paseo Colón", "http://www.fi.uba.ar/sites/default/files/styles/miniatura_noticia/public/field/image/Cass%20Technology%20Day%20-%20web.jpg?itok=kWsKFAYK","La FIUBA invita a participar del Cass Tecnology Day, que se llevará a cabo el próximo miércoles 16 de noviembre, desde las 14.30, en el Salón \"Ing. Humberto Ciancaglini\" de la sede de Av. Paseo Colón 850.\nEl encuentro, destinado a la industria eléctrica y electrónica, así como investigadores, docentes y estudiantes de dicha especialidad, contará con el siguiente cronograma de actividades: \n \n14.30 - 15.00: Acreditación\n15.00 - 15.45: Situación de la industria tecnológica en la región (Victor Grimblatt)\n15.45 - 16.30: Tech Industry and Startups in Latin America (Raul Camposano)\n16.30 - 17.00: Cafe\n17.00 - 17.45: Entrepreneurship and venture capital (James Hogan)\n17.45 - 18.30: Instrumentos de Emprendimiento (Martín Albarracín, FONSOFT)\nAcceder al formulario de inscripción", "http://www.fi.uba.ar/sites/default/files/field/image/Cass%20Technology%20Day%20-%20web.jpg"));
            sample_data = nn;
        }
        return sample_data;
    }
}
