import xml.etree.ElementTree as ET

def convert_xml_to_dot(xml_file_path, output_dot_path):
    """
    将给定的XML依赖文件转换为Graphviz .dot格式文件。

    Args:
        xml_file_path (str): 输入的XML文件路径。
        output_dot_path (str): 输出的.dot文件路径。
    """
    # 解析XML文件
    tree = ET.parse(xml_file_path)
    root = tree.getroot()

    # 用于存储所有唯一的节点
    all_nodes = set()
    # 用于存储所有的边 (from_node, to_node)
    all_edges = []

    # 遍历所有的 <file> 元素
    for file_elem in root.findall('file'):
        file_path = file_elem.get('path')
        all_nodes.add(file_path)

        # 遍历该文件下的所有 <dependency> 元素
        for dep_elem in file_elem.findall('dependency'):
            dep_path = dep_elem.get('path')
            all_nodes.add(dep_path)
            # 添加一条边: file_path -> dep_path
            all_edges.append((file_path, dep_path))

    # 开始构建 .dot 文件内容
    dot_content = []
    dot_content.append("digraph G {")
    dot_content.append('    rankdir=LR; // 从左到右布局')
    dot_content.append('    node [shape=box, style=filled, fillcolor="#E6E6FA"];')
    dot_content.append('    // 定义节点')

    # 为每个节点创建定义
    for node_path in sorted(all_nodes):
        # 使用文件名作为显示标签
        display_label = node_path.split('/')[-1]
        # 如果是外部依赖，用不同颜色标记
        if node_path.startswith("$MAVEN_REPOSITORY$"):
            fillcolor = "#FFDAB9" # 桃色
            style = "filled, dashed"
        else:
            fillcolor = "#E6E6FA" # 紫色
            style = "filled"

        dot_content.append(f'    "{node_path}" [label="{display_label}", fillcolor="{fillcolor}", style="{style}"];')

    dot_content.append('    // 定义依赖关系')
    # 为每条边创建定义
    for from_node, to_node in all_edges:
        dot_content.append(f'    "{from_node}" -> "{to_node}";')

    dot_content.append("}")

    # 将内容写入 .dot 文件
    with open(output_dot_path, 'w', encoding='utf-8') as dot_file:
        dot_file.write("\n".join(dot_content))

    print(f"转换完成！Graphviz文件已保存至: {output_dot_path}")

# 使用示例
# 请将 'deps.xml' 替换为您的实际文件路径
# 请将 'dependency_graph.dot' 替换为您希望的输出文件名
convert_xml_to_dot('H:\\A137442\\Develop\\AGI\\NARS\\opennars-304-master\\converts.local\\deps.xml', 'dependency_graph.dot')
