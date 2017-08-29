# Fall 2012 6.034 Lab 2: Search
#
# Your answers for the true and false questions will be in the following form.  
# Your answers will look like one of the two below:
#ANSWER1 = True
#ANSWER1 = False

# 1: True or false - Hill Climbing search is guaranteed to find a solution
#    if there is a solution
ANSWER1 = False

# 2: True or false - Best-first search will give an optimal search result
#    (shortest path length).
#    (If you don't know what we mean by best-first search, refer to
#     http://courses.csail.mit.edu/6.034f/ai3/ch4.pdf (page 13 of the pdf).)
ANSWER2 = False

# 3: True or false - Best-first search and hill climbing make use of
#    heuristic values of nodes.
ANSWER3 = True

# 4: True or false - A* uses an extended-nodes set.
ANSWER4 = True

# 5: True or false - Breadth first search is guaranteed to return a path
#    with the shortest number of nodes.
ANSWER5 = True

# 6: True or false - The regular branch and bound uses heuristic values
#    to speed up the search for an optimal path.
ANSWER6 = False

# Import the Graph data structure from 'search.py'
# Refer to search.py for documentation
from search import Graph

## Optional Warm-up: BFS and DFS
# If you implement these, the offline tester will test them.
# If you don't, it won't.
# The online tester will not test them.

def bfs(graph, start, goal):
    agenda = [[start]]
    cand = agenda.pop()
    while cand[-1] != goal:
        neighbors = graph.get_connected_nodes(cand[-1])
        for n in neighbors:
            if n not in set(cand):
                path = cand+[n]
                agenda = [path] + agenda
        if len(agenda) == 0:
            return []
        else:
            cand = agenda.pop()
    return cand

## Once you have completed the breadth-first search,
## this part should be very simple to complete.
def dfs(graph, start, goal):
    agenda = [[start]]
    cand = agenda.pop()
    while cand[-1] != goal:
        neighbors = graph.get_connected_nodes(cand[-1])
        for n in neighbors:
            if n not in set(cand):
                path = cand+[n]
                agenda = agenda + [path]
        if len(agenda) == 0:
            return []
        else:
            cand = agenda.pop()
    return cand


## Now we're going to add some heuristics into the search.  
## Remember that hill-climbing is a modified version of depth-first search.
## Search direction should be towards lower heuristic values to the goal.
def hill_climbing(graph, start, goal):
    agenda = [[start]]
    cand = agenda.pop()
    while cand[-1] != goal:
        neighbors = graph.get_connected_nodes(cand[-1])
        new_paths = []
        for n in neighbors:
            if n not in set(cand):
                path = cand+[n]
                new_paths.append((path,graph.get_heuristic(n,goal)))
        new_paths = sorted(new_paths, key=lambda tup: tup[1], reverse=True)
        for path in new_paths:
            agenda = agenda + [path[0]]
        if len(agenda) == 0:
            return []
        else:
            cand = agenda.pop()
    return cand

## Now we're going to implement beam search, a variation on BFS
## that caps the amount of memory used to store paths.  Remember,
## we maintain only k candidate paths of length n in our agenda at any time.
## The k top candidates are to be determined using the 
## graph get_heuristic function, with lower values being better values.
def beam_search(graph, start, goal, beam_width):
    curr_agenda = [[start]]
    next_agenda = []
    cand = curr_agenda.pop()
    while cand[-1] != goal:
        neighbors = graph.get_connected_nodes(cand[-1])
        for n in neighbors:
            if n not in set(cand):
                path = cand+[n]
                next_agenda = [(path,graph.get_heuristic(n,goal))] + next_agenda
        if len(curr_agenda) == 0 and len(next_agenda) == 0:
            return []
        elif len(curr_agenda) > 0:
            cand = curr_agenda.pop()
        else:
            next_agenda = sorted(next_agenda, key=lambda tup: tup[1])
            curr_agenda = []
            for i in range(min([beam_width,len(next_agenda)])):
                curr_agenda = [next_agenda[i][0]]+curr_agenda
            next_agenda = []
            cand = curr_agenda.pop()
    return cand


## Now we're going to try optimal search.  The previous searches haven't
## used edge distances in the calculation.

## This function takes in a graph and a list of node names, and returns
## the sum of edge lengths along the path -- the total distance in the path.
def path_length(graph, node_names):
    if len(node_names) == 1:
        return 0
    out = 0
    for i in range(len(node_names)-1):
        out += graph.get_edge(node_names[i],node_names[i+1]).length
    return out


def branch_and_bound(graph, start, goal):
    agenda = [([start],0)]
    cand = agenda.pop()[0]
    while cand[-1] != goal:
        neighbors = graph.get_connected_nodes(cand[-1])
        for n in neighbors:
            if n not in set(cand):
                path = cand+[n]
                agenda = [(path,path_length(graph,path))] + agenda
        if len(agenda) == 0:
            return []
        else:
            agenda = sorted(agenda, key=lambda tup: tup[1], reverse=True)
            cand = agenda.pop()[0]
    return cand

def a_star(graph, start, goal):
    extended = {start}
    agenda = [([start],0)]
    cand = agenda.pop()[0]
    while cand[-1] != goal:
        neighbors = graph.get_connected_nodes(cand[-1])
        for n in neighbors:
            if (n not in set(cand)) and (n not in extended):
                extended.add(n)
                path = cand+[n]
                agenda = [(path,path_length(graph,path)+graph.get_heuristic(n,goal))] + agenda
        if len(agenda) == 0:
            return []
        else:
            agenda = sorted(agenda, key=lambda tup: tup[1], reverse=True)
            cand = agenda.pop()[0]
    return cand



## It's useful to determine if a graph has a consistent and admissible
## heuristic.  You've seen graphs with heuristics that are
## admissible, but not consistent.  Have you seen any graphs that are
## consistent, but not admissible?

def is_admissible(graph, goal):
    nodes = graph.nodes
    for n in nodes:
        if graph.get_heuristic(n,goal) > path_length(graph,branch_and_bound(graph,n,goal)):
            return False
    return True

def is_consistent(graph, goal):
    edges = graph.edges
    for e in edges:
        if abs(graph.get_heuristic(e.node1,goal)-graph.get_heuristic(e.node2,goal)) > e.length:
            return False
    return True


HOW_MANY_HOURS_THIS_PSET_TOOK = '0'
WHAT_I_FOUND_INTERESTING = 'cool'
WHAT_I_FOUND_BORING = 'cool'
