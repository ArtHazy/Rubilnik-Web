import React from "react";
import { createBoard } from "@wixc3/react-board";
import { QuizTile } from "../../../2 App/ViewLibrary.jsx";

let quiz = {title:"quiz1", questions:[]}

export default createBoard({
  name: "QuizTile",
  Board: () => (
    <QuizTile quiz={quiz} ind={0} upd={() => {}} self={{}} quizzes={[]} />
  ),
  isSnippet: true,
});
