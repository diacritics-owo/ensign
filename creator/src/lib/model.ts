export const WIDTH = 20;
export const HEIGHT = 40;

export type Rectangle = [[number, number], [number, number]];
export type Dimensions = [number, number, number];
export type ModelShape = number[][];
export type Model = { offset: Dimensions; size: Dimensions }[];

export const area = (rectangle: Rectangle) => rectangle[1][0] * rectangle[1][1];
export const contains = (rectangle: Rectangle, point: [number, number]) =>
  rectangle[0][0] <= point[0] &&
  point[0] <= rectangle[0][0] + rectangle[1][0] - 1 &&
  rectangle[0][1] <= point[1] &&
  point[1] <= rectangle[0][1] + rectangle[1][1] - 1;

export const rectangle = (modelShape: ModelShape): Rectangle => {
  const hist = (heights: number[], heightCoordinates: number[]): Rectangle => {
    const n = heights.length;

    let stack: number[][] = [];
    const left: number[] = [];
    const right: number[] = [];

    for (let i = 0; i < n; i++) {
      while (stack.length > 0 && stack[stack.length - 1][0] >= heights[i]) {
        stack.pop();
      }

      left.push(stack.length === 0 ? -1 : stack[stack.length - 1][1]);
      stack.push([heights[i], i]);
    }

    stack = [];

    for (let i = n - 1; i >= 0; i--) {
      while (stack.length > 0 && stack[stack.length - 1][0] >= heights[i]) {
        stack.pop();
      }

      right[i] = stack.length === 0 ? n : stack[stack.length - 1][1];
      stack.push([heights[i], i]);
    }

    let ans: Rectangle = [
      [0, 0],
      [0, 0],
    ];

    for (let i = 0; i < n; i++) {
      const diff = right[i] - left[i] - 1;

      if (heights[i] * diff > area(ans)) {
        ans = [
          [left[i] + 1, heightCoordinates[i]],
          [diff, heights[i]],
        ];
      }
    }

    return ans;
  };

  let result: Rectangle = [
    [0, 0],
    [0, 0],
  ];

  const heights: number[] = new Array(WIDTH).fill(0);
  const heightCoordinates: number[] = new Array(WIDTH).fill(0);

  for (let i = 0; i < HEIGHT; i++) {
    for (let j = 0; j < WIDTH; j++) {
      if (modelShape[i][j] === 0) {
        heights[j] = 0;
      } else {
        if (heights[j] === 0) {
          heightCoordinates[j] = i;
        }

        heights[j] += modelShape[i][j];
      }
    }

    const updated = hist(heights, heightCoordinates);

    if (area(updated) > area(result)) {
      result = updated;
    }
  }

  return result;
};

export const toModelDirect = (modelShape: ModelShape) =>
  modelShape
    .map((row, j) => row.map((b, i) => (b ? { offset: [i, j, 0], size: [1, 1, 1] } : null)))
    .flat()
    .filter((x) => x !== null) as Model;

export const toModel = (modelShape: ModelShape) => {
  let initial = toModelDirect(modelShape);

  // 10 passes seems fair
  for (let i = 0; i < 10; i++) {
    const previous = initial.filter(({ offset, size }) => ![size[0], size[1]].includes(1));
    const largest = rectangle(
      fromModel(initial.filter(({ offset, size }) => [size[0], size[1]].includes(1))),
    );

    if (
      !!previous.find(
        ({ offset, size }) =>
          offset[0] === largest[0][0] &&
          offset[1] === largest[0][1] &&
          size[0] === largest[1][0] &&
          size[1] === largest[1][1],
      )
    ) {
      break;
    }

    initial = initial.filter(({ offset, size }) => !contains(largest, [offset[0], offset[1]]));

    initial.push({
      offset: [largest[0][0], largest[0][1], 0],
      size: [largest[1][0], largest[1][1], 1],
    });
  }

  return initial.filter(({ size }) => !size.includes(0));
};

export const fromModel = (model: Model) =>
  [...Array(HEIGHT)].map((_, j) =>
    [...Array(WIDTH)].map((_, i) =>
      !!model.find(({ offset, size }) =>
        contains(
          [
            [offset[0], offset[1]],
            [size[0], size[1]],
          ],
          [i, j],
        ),
      )
        ? 1
        : 0,
    ),
  );
