package track.container;

import track.container.config.Bean;
import track.container.config.CycleReferenceException;
import track.container.config.Property;
import track.container.config.ValueType;

import java.util.*;

public class BeanGraph {
    private Map<BeanVertex, List<BeanVertex>> vertices;
    private LinkedList<Bean> sorted;

    public BeanGraph() {
        vertices = new HashMap<>();
        sorted = new LinkedList<>();
    }

    public BeanGraph(final List<Bean> beans) {
        this();

        //добавляем все вершины графа
        for (Bean bean : beans) {
            BeanVertex beanVertex = new BeanVertex(bean);
            addVertex(beanVertex);
        }

        //цикл по вершинам (бинам) в графе
        for (BeanVertex beanVertex : vertices.keySet()) {
            //цикл по полям конкретного бина
            for (Property property : beanVertex.getValue().getProperties().values()) {
                if (property.getType() == ValueType.REF) {
                    String refBeanName = property.getValue();
                    //поиск бина на который имеет ссылку конкретный бин
                    for (BeanVertex refBeanVertex : vertices.keySet()) {
                        if (refBeanVertex.getValue().getId().equals(refBeanName)) {
                            addEdge(beanVertex, refBeanVertex);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void addVertex(final BeanVertex bean) {
        vertices.put(bean, new ArrayList<>());
    }

    public boolean addEdge(final BeanVertex from, BeanVertex to) {
        if (vertices.get(from) != null) {
            vertices.get(from).add(to);
            return true;
        }
        return false;
    }

    public boolean isLinked(final BeanVertex bean) {
        return getLinkedVertices(bean) != null;
    }

    public List<BeanVertex> getLinkedVertices(final BeanVertex bean) {
        return vertices.get(bean);
    }

    public int size() {
        return vertices.size();
    }

    public LinkedList<Bean> topologicalSort() throws CycleReferenceException {
        boolean Cycle;

        for (BeanVertex bean : vertices.keySet()) {
            Cycle = dfs(bean);
            if (Cycle)
                throw new CycleReferenceException("Cycle reference found in " + bean.getValue().getId());
        }
        return sorted;
    }

    private boolean dfs(final BeanVertex bean) {
        if (bean.getState() == BeanVertex.State.VISITED)
            return true;
        if (bean.getState() == BeanVertex.State.MARKED)
            return false;

        bean.setState(BeanVertex.State.VISITED);
        for (BeanVertex linkedBean : getLinkedVertices(bean)) {
            if (dfs(linkedBean))
                return true;
        }
        sorted.offer(bean.getValue());
        bean.setState(BeanVertex.State.MARKED);
        return false;
    }
}
