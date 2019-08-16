package at.dalex.grape.sdk.window.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class EventManager {

    /**
     * Retrieves all methods responsible for handling events in the classes given
     * in the passed list.
     *
     * @param eventListeners List containing event listener instances.
     * @return List containing all event handler methods
     */
    public static ArrayList<Method> getEventHandlerMethods(ArrayList<EventListener> eventListeners) {
        ArrayList<Method> handlerMethods = new ArrayList<>();

        for (EventListener listener : eventListeners) {
            handlerMethods.addAll(getEventHandlerMethods(listener));
        }

        return handlerMethods;
    }

    /**
     * Retrieves all methods responsible for handling events in the class
     * of the given object.
     * @return List containing all event handler methods
     */
    public static ArrayList<Method> getEventHandlerMethods(EventListener eventListener) {
        ArrayList<Method> handlerMethods = new ArrayList<>();

        //Retrieve all candidate methods from the class of the given instance
        Class<?> listenerClass = eventListener.getClass();
        Method[] listenerMethods = listenerClass.getMethods();

        //Only add methods to the list that are correctly annotated
        for (Method method : listenerMethods) {
            //Check if method is annotated with an 'EventHandler' annotation
            if (method.isAnnotationPresent(EventHandler.class)) {
                handlerMethods.add(method);
            }
        }

        return handlerMethods;
    }

    /**
     * Calls all handler methods where the event types are matching.
     *
     * @param handlerMethods List of handler methods.
     * @param invocationEvent The instance of the event responsible for calling the handlers.
     */
    public static void callHandlerMethods(EventListener listenerInstance, ArrayList<Method> handlerMethods, EventBase invocationEvent) {
        for (Method handlerMethod : handlerMethods) {
            //Break if event has been cancelled by a handler method
            if (invocationEvent.isCancelled())
                break;

            //Skip if method does not contains parameter with the type of the invocation event
            Class<?>[] parameterTypes = handlerMethod.getParameterTypes();
            if (!Arrays.asList(parameterTypes).contains(invocationEvent.getClass()))
                continue;

            //Try to call method and pass invocation event as parameter
            try {
                handlerMethod.invoke(listenerInstance, invocationEvent);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println("Unable to invoke event handler method!");
                e.printStackTrace();
            }
        }
    }
}
