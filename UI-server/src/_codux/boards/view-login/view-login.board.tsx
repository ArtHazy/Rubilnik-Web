import React from "react";
import { createBoard } from "@wixc3/react-board";
import { ViewLogin } from "../../../1 Authentication/ViewLogin.jsx";

export default createBoard({
  name: "ViewLogin",
  Board: () => <ViewLogin />,
  isSnippet: true,
});
