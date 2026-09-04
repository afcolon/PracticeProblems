import { useEffect, useRef } from "react";

export default function Modal({ isOpen, onClose, children }) {
    const dialogRef = useRef(null);

    // Synchronize the native dialog state with the React isOpen prop
    useEffect(() => {
        const dialog = dialogRef.current;
        if (!dialog) return;

        if (isOpen) {
            dialog.showModal(); // Opens the backdrop and traps focus
        } else {
            dialog.close();
        }
    }, [isOpen]);

    // Handle closure when user presses the Esc key
    const handleCancel = (e) => {
        e.preventDefault();
        onClose();
    };

    return (
        <dialog ref={dialogRef} onCancel={handleCancel} className="modal-box">
        <div className="modal-content">
            {children}
            <button onClick={onClose} className="close-btn">Close</button>
        </div>
        </dialog>
    );
}
