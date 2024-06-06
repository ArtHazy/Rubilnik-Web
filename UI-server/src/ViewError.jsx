export const ViewError = ({code, text}) => 
    <div className="ViewError">
        <h1>Oops</h1>
        <h1>{code}</h1>
        {text}
    </div>