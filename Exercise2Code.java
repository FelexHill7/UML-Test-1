import java.util.ArrayList;
import java.util.List;

// -------------------------
// Agent Interface
// -------------------------
interface Agent {
    String respond(String prompt);
}

// -------------------------
// Concrete Component
// -------------------------
class BasicAgent implements Agent {
    @Override
    public String respond(String prompt) {
        System.out.println("BasicAgent: responding to prompt");
        return "LLM response to: " + prompt;
    }
}

// -------------------------
// Abstract Decorator
// -------------------------
abstract class AgentDecorator implements Agent {
    protected Agent agent;

    public AgentDecorator(Agent agent) {
        this.agent = agent;
    }

    @Override
    public String respond(String prompt) {
        return agent.respond(prompt);
    }
}

// -------------------------
// Concrete Decorators
// -------------------------
class RAGDecorator extends AgentDecorator {
    public RAGDecorator(Agent agent) {
        super(agent);
    }

    @Override
    public String respond(String prompt) {
        String result = super.respond(prompt);
        System.out.println("RAGDecorator: adding retrieval-augmented content");
        return result + " + RAG";
    }
}

class ToolsDecorator extends AgentDecorator {
    public ToolsDecorator(Agent agent) {
        super(agent);
    }

    @Override
    public String respond(String prompt) {
        String result = super.respond(prompt);
        System.out.println("ToolsDecorator: using external tools");
        return result + " + Tools";
    }
}

class MemoryDecorator extends AgentDecorator {
    public MemoryDecorator(Agent agent) {
        super(agent);
    }

    @Override
    public String respond(String prompt) {
        String result = super.respond(prompt);
        System.out.println("MemoryDecorator: remembering prompt");
        return result + " + Memory";
    }
}

class HumanDecorator extends AgentDecorator {
    public HumanDecorator(Agent agent) {
        super(agent);
    }

    @Override
    public String respond(String prompt) {
        String result = super.respond(prompt);
        System.out.println("HumanDecorator: requesting human assistance");
        return result + " + Human";
    }
}

// -------------------------
// AgentConfigurator
// -------------------------
class AgentConfigurator {
    public List<String> identifyAids(String prompt) {
        List<String> aids = new ArrayList<>();
        if (prompt.contains("RAG") || prompt.contains("legal")) {
            aids.add("RAG");
        }
        if (prompt.contains("tools") || prompt.contains("schedule")) {
            aids.add("Tools");
        }
        if (prompt.contains("remember")) {
            aids.add("Memory");
        }
        if (prompt.contains("human") || prompt.contains("legal")) {
            aids.add("Human");
        }
        return aids;
    }

    public Agent buildAgent(String prompt) {
        Agent agent = new BasicAgent();
        for (String aid : identifyAids(prompt)) {
            switch (aid) {
                case "RAG":
                    agent = new RAGDecorator(agent);
                    break;
                case "Tools":
                    agent = new ToolsDecorator(agent);
                    break;
                case "Memory":
                    agent = new MemoryDecorator(agent);
                    break;
                case "Human":
                    agent = new HumanDecorator(agent);
                    break;
            }
        }
        return agent;
    }

    public String respond(String prompt) {
        Agent agent = buildAgent(prompt);
        return agent.respond(prompt);
    }
}

// -------------------------
// Client
// -------------------------
public class Exercise2Code {
    public static void main(String[] args) {
        AgentConfigurator configurator = new AgentConfigurator();

        System.out.println("Prompt 1: RAG + Tools");
        String response1 = configurator.respond("Explain climate change with RAG and tools.");
        System.out.println("Final Response: " + response1);

        System.out.println("\nPrompt 2: Tools + Memory");
        String response2 = configurator.respond("Schedule my meetings with tools and remember notes.");
        System.out.println("Final Response: " + response2);

        System.out.println("\nPrompt 3: RAG + Human");
        String response3 = configurator.respond("Solve legal query about GDPR using RAG and human assistance.");
        System.out.println("Final Response: " + response3);
    }
}
