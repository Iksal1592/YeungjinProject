document.addEventListener('DOMContentLoaded', function() {
    loadWordsForPeriod(0); // 페이지 로드 시 초기 워드 클라우드 생성
});

function loadWordsForPeriod(months) {
    fetch(`/word-frequencies?months=${months}`)
        .then(response => response.json())
        .then(frequencies => {
            var wordData = Object.keys(frequencies).map(word => {
                var size = Math.min(frequencies[word] * 10, 100); // 최대 크기 제한
                return {text: word, size: size};
            });

            createWordCloud(wordData);
        });
}

document.getElementById('one').addEventListener('click', () => loadWordsForPeriod(1));
document.getElementById('two').addEventListener('click', () => loadWordsForPeriod(2));
document.getElementById('three').addEventListener('click', () => loadWordsForPeriod(3));
document.getElementById('all').addEventListener('click', () => loadWordsForPeriod(0));

function createWordCloud(words) {
    d3.select("#wordCloud").selectAll("*").remove();

    // 파란색 계열의 색상으로 변경
    var colorScale = d3.scaleLinear()
        .domain([10, 100])
        .range(["lightblue", "navy"]); // 밝은 파란색에서 진한 파란색으로

    var layout = d3.layout.cloud()
        .size([800, 600])
        .words(words)
        .padding(5)
        .rotate(function() {
            // 0도 또는 90도로만 회전
            return (~~(Math.random() * 2) * 90);
        })
        .fontSize(d => d.size)
        .on("end", draw);

    layout.start();

    function draw(words) {
        var wordElements = d3.select("#wordCloud").append("svg")
            .attr("width", layout.size()[0])
            .attr("height", layout.size()[1])
            .append("g")
            .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
            .selectAll("text")
            .data(words)
            .enter().append("text")
            .style("font-size", d => d.size + "px")
            .style("fill", d => colorScale(d.size)) // 색상을 크기에 따라 결정
            .attr("text-anchor", "middle")
            .attr("transform", d => `translate(${d.x}, ${d.y}) rotate(${d.rotate})`)
            .text(d => d.text)
            .on("click", d => onWordClick(d)); // 클릭 이벤트 핸들러 추가

        wordElements.on("mouseover", function(d) {
            d3.select(this).style("font-size", d.size * 1.2 + "px"); // 20% 증가
        });

        wordElements.on("mouseout", function(d) {
            d3.select(this).style("font-size", d.size + "px"); // 원래 크기로 복귀
        });
    }

    function onWordClick(word) {
        // 단어 클릭 시 수행할 동작
        window.open(`https://www.google.com/search?q=${word.text}`, '_blank');
    }
}
